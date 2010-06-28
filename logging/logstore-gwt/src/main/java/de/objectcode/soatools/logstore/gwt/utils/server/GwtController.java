package de.objectcode.soatools.logstore.gwt.utils.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtController extends RemoteServiceServlet implements Controller,
		BeanNameAware, ApplicationContextAware, ServletContextAware {
	private static final long serialVersionUID = -2752883309003572211L;

	private ServletContext context;
	private String beanName;
	private RemoteService remoteService;

	/**
	 * Implements Spring Controller interface method.
	 * 
	 * Call GWT's RemoteService doPost() method and return null.
	 * 
	 * @param request
	 *            current HTTP request
	 * @param response
	 *            current HTTP response
	 * @return a ModelAndView to render, or null if handled directly
	 * @throws Exception
	 *             in case of errors
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		doPost(request, response);
		return null; // response handled by GWT RPC over XmlHttpRequest
	}

	@Override
	public String processCall(String payload) throws SerializationException {
		try {
			RPCRequest rpcRequest = RPC.decodeRequest(payload, this.getClass(),
					this);
			onAfterRequestDeserialized(rpcRequest);
			return RPC.invokeAndEncodeResponse(remoteService, rpcRequest.getMethod(),
					rpcRequest.getParameters(), rpcRequest
							.getSerializationPolicy(), rpcRequest.getFlags());
		} catch (IncompatibleRemoteServiceException ex) {
			log(
					"An IncompatibleRemoteServiceException was thrown while processing this call.",
					ex);
			return RPC.encodeResponseForFailure(null, ex);
		}
	}

	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	public ServletContext getServletContext() {
		return context;
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		remoteService = (RemoteService) context.getBean(beanName);
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
}