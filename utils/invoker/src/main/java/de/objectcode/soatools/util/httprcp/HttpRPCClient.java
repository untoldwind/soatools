package de.objectcode.soatools.util.httprcp;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionLifecycleException;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.http.HttpClientFactory;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.javabean.BeanAccessor;
import org.milyn.resource.URIResourceLocator;

import com.thoughtworks.xstream.XStream;

import de.objectcode.soatools.util.LimitedStreamReader;

public class HttpRPCClient extends AbstractActionPipelineProcessor {
	private final static String BAD_RESPONSE_PATTER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "\n"
			+ "<response message-id=\"1001\" xmlns=\"http://schemas.alopa.com/Inbound\">\n"
			+ "  <status ErrorCode=\"{0}\" ErrorMessage=\"Http-Remote call return bad response code: &lt;&lt;{1}&gt;&gt;\"/>\n"
			+ "</response>";

	private final static Log LOGGER = LogFactory.getLog(HttpRPCClient.class);
	private final Map<String, String> additionalHeaderDatas = new HashMap<String, String>();
	private final boolean attachRequestResponse;
	private final String baseUrlPropetyName;
	private final String encoding;
	private final boolean failOnNotOk;
	private final HttpClient httpClient;
	private final Properties httpClientProps = new Properties();
	private boolean isSticky = false;
	private final HttpRPCMethod method;
	private final MessagePayloadProxy payloadProxy;
	private final Smooks requestSmooks;
	private final long responseLimit;
	private final Smooks responseSmooks;
	private final boolean runtimeOnErrorStatus;
	private final boolean runtimeOnIOFailure;
	private final String serviceUrl;
	private final String statusCodeLocation;
	private final String stickyBaseUrlPropertyName;
	private final String url;

	public HttpRPCClient(final ConfigTree config) throws Exception {
		try {
			this.baseUrlPropetyName = config.getAttribute("baseUrlProperty");
			this.stickyBaseUrlPropertyName = config
					.getAttribute("stickyBaseUrlProperty");
			this.serviceUrl = config.getAttribute("serviceUrl");
			this.url = config.getAttribute("url");

			if (this.url == null
					&& (this.baseUrlPropetyName == null || this.serviceUrl == null)) {
				throw new ConfigurationException(
						"Either specify url or baseUrlProperty with serviceUrl");
			}
			this.method = HttpRPCMethod.valueOf(config
					.getRequiredAttribute("method"));
			this.responseLimit = config.getLongAttribute("response-limit",
					10 * 1024L * 1024L);
			this.encoding = config.getAttribute("encoding", "UTF-8");
			this.statusCodeLocation = config.getAttribute(
					"status-code-location", "http-status-code");
			this.failOnNotOk = config.getBooleanAttribute("fail-on-not-ok",
					true);
			this.runtimeOnErrorStatus = config.getBooleanAttribute(
					"runtime-on-error-status", true);
			this.runtimeOnIOFailure = config.getBooleanAttribute(
					"runtime-on-io-failure", true);
			this.attachRequestResponse = config.getBooleanAttribute(
					"attach-request-response", true);
			this.payloadProxy = new MessagePayloadProxy(config);
			final String requestSmooksResource = config
					.getAttribute("requestSmooks");
			// TODO: Find out if customer need reloading of smooks
			if (requestSmooksResource != null) {
				try {
					this.requestSmooks = new Smooks();
					this.requestSmooks.addConfigurations("smooks-resource",
							new URIResourceLocator()
									.getResource(requestSmooksResource));
					this.requestSmooks
							.addConfigurations(
									"cdu-creators",
									new URIResourceLocator()
											.getResource("/META-INF/smooks-creators.xml"));
				} catch (final Exception e) {
					throw new ConfigurationException(
							"Failed to initialize Smooks", e);
				}
			} else {
				this.requestSmooks = null;
			}
			final String responseSmooksResource = config
					.getAttribute("responseSmooks");
			if (responseSmooksResource != null) {
				try {
					this.responseSmooks = new Smooks();
					this.responseSmooks.addConfigurations("smooks-resource",
							new URIResourceLocator()
									.getResource(responseSmooksResource));
					this.responseSmooks
							.addConfigurations(
									"cdu-creators",
									new URIResourceLocator()
											.getResource("/META-INF/smooks-creators.xml"));
				} catch (final Exception e) {
					throw new ConfigurationException(
							"Failed to initialize Smooks", e);
				}
			} else {
				this.responseSmooks = null;
			}

			extractHttpClientProps(config);
			this.httpClient = HttpClientFactory
					.createHttpClient(this.httpClientProps);

			final String maxConnectionsPerHost = config
					.getAttribute("max-connections-per-host");
			final String maxTotalConnections = config
					.getAttribute("max-total-connections");

			if (maxConnectionsPerHost != null || maxTotalConnections != null) {
				final HttpConnectionManagerParams params = new HttpConnectionManagerParams();

				params
						.setDefaultMaxConnectionsPerHost(maxConnectionsPerHost != null ? Integer
								.parseInt(maxConnectionsPerHost)
								: MultiThreadedHttpConnectionManager.DEFAULT_MAX_HOST_CONNECTIONS);
				params
						.setMaxTotalConnections(maxTotalConnections != null ? Integer
								.parseInt(maxTotalConnections)
								: MultiThreadedHttpConnectionManager.DEFAULT_MAX_TOTAL_CONNECTIONS);

				this.httpClient.getHttpConnectionManager().setParams(params);
			}

			final ConfigTree[] additionHeaderDatasConfigTrees = config
					.getChildren("additional-header-data");

			for (final ConfigTree additionHeaderDatasConfigTree : additionHeaderDatasConfigTrees) {
				final String key = additionHeaderDatasConfigTree
						.getAttribute("key");
				final String value = additionHeaderDatasConfigTree
						.getAttribute("value");

				this.additionalHeaderDatas.put(key, value);
			}

		} catch (final Exception ex) {
			final String configTreeAttributes = getConfgTreeAsString(config);

			LOGGER.error("HttpRPCClient: ex = ", ex);
			LOGGER.error(">>>>    Occured with this configuration:\n"
					+ configTreeAttributes);

			throw ex;
		}
	}

	private void extractHttpClientProps(final ConfigTree config) {
		final ConfigTree[] httpClientConfigTrees = config
				.getChildren("http-client-property");

		// TODO: Add authentication?
		// TODO: We use the initial url here. This might lead to a problem if we
		// switch protocal (http -> https) or authentication at runtime
		this.httpClientProps.setProperty(HttpClientFactory.TARGET_HOST_URL,
				getTargetUrl());

		// The HttpClient properties are attached under the factory class/impl
		// property as <http-client-property name="x" value="y" /> nodes
		for (final ConfigTree httpClientProp : httpClientConfigTrees) {
			final String propName = httpClientProp.getAttribute("name");
			final String propValue = httpClientProp.getAttribute("value");

			if (propName != null && propValue != null) {
				this.httpClientProps.setProperty(propName, propValue);
			}
		}
	}

	private String getConfgTreeAsString(final ConfigTree config) {
		final StringBuffer retVal = new StringBuffer();

		final ConfigTree[] allConfigChildren = config.getAllChildren();

		for (int i = 0, size = allConfigChildren.length; i < size; i++) {
			retVal.append(getConfgTreeAsString(allConfigChildren[i]));
		}

		final Set<String> attributeNames = config.getAttributeNames();

		for (final Iterator<String> attributeIter = attributeNames.iterator(); attributeIter
				.hasNext();) {
			final String attributeName = attributeIter.next();

			retVal.append("AttribName: " + attributeName + "\t"
					+ "AttribValue: " + config.getAttribute(attributeName)
					+ "\n");
		}

		return retVal.toString();
	}

	private String getTargetUrl() {
		if (this.url != null) {
			return this.url;
		}

		String baseUrl = null;

		if (stickyBaseUrlPropertyName != null
				&& stickyBaseUrlPropertyName.trim().length() != 0
				&& this.isSticky) {
			baseUrl = System.getProperty(this.stickyBaseUrlPropertyName);
		} else {
			baseUrl = System.getProperty(this.baseUrlPropetyName);
		}

		if (baseUrl == null) {
			LOGGER.error("System property " + this.baseUrlPropetyName
					+ " not set");
			throw new RuntimeException("System property "
					+ this.baseUrlPropetyName + " not set");
		}

		String retVal = baseUrl + "/" + this.serviceUrl;
		LOGGER.debug("getTargetUrl: @return retVal = " + retVal);
		return retVal;
	}

	@Override
	public void initialise() throws ActionLifecycleException {
		super.initialise();
	}

	protected String invokeEndpointGet(final Message message,
			final Map<?, ?> params) throws Exception {
		final StringBuilder getUrl = new StringBuilder(getTargetUrl());

		if (params != null) {
			getUrl.append('?');
			final Iterator<?> keyIt = params.keySet().iterator();

			try {
				while (keyIt.hasNext()) {
					final Object key = keyIt.next();
					final Object value = params.get(key);
					getUrl.append(URLEncoder.encode(key.toString(),
							this.encoding));
					getUrl.append('=');
					getUrl.append(URLEncoder.encode(value.toString(),
							this.encoding));
					if (keyIt.hasNext()) {
						getUrl.append('&');
					}
				}
			} catch (final UnsupportedEncodingException e) {
				throw new ActionProcessingException("Wrong encoding.", e);
			}
		}
		final GetMethod get = new GetMethod(getUrl.toString());
		if (this.attachRequestResponse) {
			message.getAttachment().put("http-get-url", getUrl.toString());
		}
		try {
			LOGGER.debug("invokeEndpointGet  : @memo >>> send over http");

			long start = new Date().getTime();
			final int result = this.httpClient.executeMethod(get);
			long end = new Date().getTime();

			LOGGER.debug("invokeEndpointGet  : @memo Http call take <<millis: "
					+ (end - start) + ">>");

			if (result >= HttpStatus.SC_BAD_REQUEST
					&& this.runtimeOnErrorStatus) {
				throw new RuntimeException("Got error result " + result
						+ " from '" + getTargetUrl() + "'");
			}

			message.getBody().add(this.statusCodeLocation, result);
			if (result != HttpStatus.SC_OK) {
				if (this.failOnNotOk) {
					throw new ActionProcessingException(
							"Received status code '" + result
									+ "' on HTTP (POST) request to '"
									+ getTargetUrl() + "'.");
				}
				LOGGER.warn("Received status code '" + result
						+ "' on HTTP (POST) request to '" + getTargetUrl()
						+ "'.");
			}
			final LimitedStreamReader reader = new LimitedStreamReader(get
					.getResponseBodyAsStream(), this.responseLimit);
			reader.perform();

			final String response = new String(reader.getResult(),
					this.encoding);

			if (this.attachRequestResponse) {
				message.getAttachment().put("http-get-response", response);
			}

			return response;
		} catch (final IOException e) {
			if (this.runtimeOnIOFailure) {
				throw new RuntimeException("Failed to invoke Endpoint: '"
						+ getTargetUrl() + "'.", e);
			} else {
				throw new ActionProcessingException(
						"Failed to invoke Endpoint: '" + getTargetUrl() + "'.",
						e);
			}
		} finally {
			get.releaseConnection();
		}
	}

	protected String invokeEndpointPost(final Message message,
			final String request) throws Exception {
		if (this.attachRequestResponse) {
			message.getAttachment().put("http-put-url", getTargetUrl());
			message.getAttachment().put("http-put-request", request);
		}
		final PostMethod post = new PostMethod(getTargetUrl());

		post.setRequestHeader("Content-Type", "text/xml;charset="
				+ this.encoding);

		final Set<Entry<String, String>> additionHeaderDataSet = this.additionalHeaderDatas
				.entrySet();

		for (final Iterator<Entry<String, String>> additionalHeaderDataIter = additionHeaderDataSet
				.iterator(); additionalHeaderDataIter.hasNext();) {
			final Entry<String, String> entry = additionalHeaderDataIter.next();

			final String key = entry.getKey();
			final String value = entry.getValue();

			post.setRequestHeader(key, value);
		}

		try {
			post.setRequestEntity(new StringRequestEntity(request, "text/xml",
					"UTF-8"));
		} catch (final UnsupportedEncodingException e) {
			throw new ActionProcessingException("Wrong encoding.", e);
		}

		try {
			final int result = this.httpClient.executeMethod(post);

			if (result >= HttpStatus.SC_BAD_REQUEST
					&& this.runtimeOnErrorStatus) {
				throw new RuntimeException("Got error result " + result
						+ " from '" + getTargetUrl() + "'");
			}

			// TODO: Maybe use ObjectMapper
			message.getBody().add(this.statusCodeLocation, result);

			String response = null;

			if (result == HttpStatus.SC_OK) {
				final LimitedStreamReader reader = new LimitedStreamReader(post
						.getResponseBodyAsStream(), this.responseLimit);
				reader.perform();

				response = new String(reader.getResult(), this.encoding);
			} else {
				response = MessageFormat.format(BAD_RESPONSE_PATTER,
						new Object[] { "9999", result });
			}

			if (this.attachRequestResponse) {
				message.getAttachment().put("http-put-response", response);
			}

			return response;
		} catch (final IOException e) {
			if (this.runtimeOnIOFailure) {
				throw new RuntimeException("Failed to invoke Endpoint: '"
						+ getTargetUrl() + "'.", e);
			} else {
				throw new ActionProcessingException(
						"Failed to invoke Endpoint: '" + getTargetUrl() + "'.",
						e);
			}
		} finally {
			post.releaseConnection();
		}
	}

	public synchronized Message process(final Message message)
			throws ActionProcessingException {
		Object params = null;

		final Boolean isStickyZipCode = (Boolean) message.getBody().get(
				"isStickyZipCodeForTargetSwitch");

		LOGGER.debug("process  : @memo this.isSticky = " + this.isSticky);

		if (isStickyZipCode != null && isStickyZipCode.equals(Boolean.TRUE)) {
			this.isSticky = true;

		} else {
			this.isSticky = false;
		}

		try {
			try {
				params = this.payloadProxy.getPayload(message);
			} catch (final MessageDeliverException e) {
			}

			switch (this.method) {
			case GET: {
				if (!(params instanceof Map)) {
					throw new ActionProcessingException(
							"Parameters has to be a map");
				}
				final String response = invokeEndpointGet(message,
						(Map<?, ?>) params);

				processResponse(message, response);
				break;
			}
			case POST: {
				if (params == null) {
					throw new ActionProcessingException(
							"Parameters must not be null");
				}
				String content = null;
				if (this.requestSmooks != null) {
					final ExecutionContext context = this.requestSmooks
							.createExecutionContext();
					for (final String name : message.getBody().getNames()) {
						BeanAccessor.addBean(name, message.getBody().get(name),
								context, false);
					}

					String input;
					if (params instanceof String) {
						input = (String) params;
					} else {
						final XStream xstream = new XStream();
						input = xstream.toXML(params);
					}

					final StringWriter out = new StringWriter();
					this.requestSmooks.filter(new StreamSource(
							new StringReader(input)), new StreamResult(out),
							context);
					content = out.toString();
				} else {
					content = params.toString();
				}
				// TODO write POST content to message attachment
				final String response = invokeEndpointPost(message, content);

				// TODO copy response content to message attachment
				processResponse(message, response);
				break;
			}
			}
		} catch (final RuntimeException ex) {
			LOGGER.error("process: ex = ", ex);
			throw ex;
		} catch (final Exception ex) {
			new ActionProcessingException(ex);
		}

		return message;
	}

	@SuppressWarnings("unchecked")
	protected void processResponse(final Message message, final String response)
			throws ActionProcessingException {
		if (this.responseSmooks != null) {
			final ExecutionContext context = this.responseSmooks
					.createExecutionContext();

			this.responseSmooks.filter(new StreamSource(new StringReader(
					response)), new DOMResult(), context);

			final Map<String, Object> beanMap = BeanAccessor.getBeans(context);

			if (beanMap != null) {
				final Iterator<Map.Entry<String, Object>> beans = beanMap
						.entrySet().iterator();
				while (beans.hasNext()) {
					final Map.Entry<String, Object> entry = beans.next();
					final String key = entry.getKey();

					if (LOGGER.isDebugEnabled()
							&& message.getBody().get(key) != null) {
						LOGGER.debug("Outputting Java object to '" + key
								+ "'.  Overwritting existing value.");
					}
					message.getBody().add(key, entry.getValue());
				}
			}
		}

		try {
			this.payloadProxy.setPayload(message, response);
		} catch (final MessageDeliverException e) {
			throw new ActionProcessingException(e);
		}
	}
}
