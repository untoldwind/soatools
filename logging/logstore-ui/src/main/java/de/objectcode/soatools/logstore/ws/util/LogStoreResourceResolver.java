package de.objectcode.soatools.logstore.ws.util;

import java.net.URL;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.facelets.impl.DefaultResourceResolver;

import de.objectcode.soatools.logstore.ILogStoreViewRepository;

public class LogStoreResourceResolver extends DefaultResourceResolver {

	private final static Log LOG = LogFactory
			.getLog(LogStoreResourceResolver.class);

	private final static String PREFIX = "/decorators/";

	@Override
	public URL resolveUrl(String uri) {
		if (uri != null && uri.startsWith(PREFIX)) {
			String service = uri.substring(PREFIX.length());
			String decorator = null;

			try {
				InitialContext ctx = new InitialContext();

				ILogStoreViewRepository logStoreViewRepository = (ILogStoreViewRepository) ctx
						.lookup(ILogStoreViewRepository.JNDI_NAME);
				
				decorator = logStoreViewRepository.getDecoratorResourcePath(service);
			} catch (Exception e) {
				LOG.error("Exception", e);
			}


			if (decorator != null) {
				URL resource = null;

				try {
					resource = Thread.currentThread().getContextClassLoader()
							.getResource(decorator);
				} catch (Exception e) {
					LOG.error("Exception", e);
				}

				if (resource == null) {
					LOG.warn(service + " has configured " + decorator
							+ " that can not be found in classpath");
				} else
					return resource;
			}

			return super.resolveUrl("/secure/decorators/empty.xhtml");
		}

		return super.resolveUrl(uri);
	}

}
