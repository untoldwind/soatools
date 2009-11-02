package de.objectcode.soatools.msmq.actions;

import java.text.MessageFormat;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;
import org.jboss.soa.esb.message.mapping.ObjectMapper;
import org.jboss.soa.esb.message.mapping.ObjectMappingException;


import com.thoughtworks.xstream.XStream;

import de.objectcode.soatools.msmq.outgoing.MsmqOutGatewayPortType;
import de.objectcode.soatools.msmq.outgoing.MsmqOutGatewayService;
import de.objectcode.soatools.msmq.outgoing.MsmqOutMessage;

public class MsmqOutGatewayAction extends AbstractActionPipelineProcessor
{
  /** Logger for this class. */
  private static final Log LOGGER = LogFactory.getLog(MsmqOutGatewayAction.class);
  private static final Log SNMPTRAP_CLEAR_APPENDER = LogFactory.getLog("snmptrap.clear.appender");
  private static final Log SNMPTRAP_ERROR_APPENDER = LogFactory.getLog("snmptrap.error.appender");
  private static String WEBSERVICE_NOT_AVAILABLE_ERROR_MSG = null;
  private static final String WEBSERVICE_NOT_AVAILABLE_MSG = "The webservice is not available <<exception: {0}>>";

  private final String destination;

  private final String fixLabel;

  private final MsmqOutGatewayPortType gateway;

  private final String labelPath;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final MessagePayloadProxy payloadProxy;

  private final MsmqOutGatewayService service;

  public MsmqOutGatewayAction(final ConfigTree config) throws ConfigurationException
  {
    final String endPointUrl = config.getRequiredAttribute("endpoint-url");
    this.destination = config.getRequiredAttribute("destination");
    this.labelPath = config.getAttribute("label-path");
    this.fixLabel = config.getAttribute("fix-label");

    this.payloadProxy = new MessagePayloadProxy(config);
    this.service = new MsmqOutGatewayService();
    this.gateway = this.service.getMsmqOutGatewayPort();
    final BindingProvider bp = (BindingProvider) this.gateway;
    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointUrl);
  }

  public Message process(final Message message) throws ActionProcessingException
  {
    // LOGGER.debug("process: @param message = " + message);

    final MsmqOutMessage msmqMessage = new MsmqOutMessage();

    try {
      if (this.fixLabel != null) {
        LOGGER.debug("process  : @memo this.fixLabel = " + this.fixLabel);
        msmqMessage.setLabel(this.fixLabel);
      } else if (this.labelPath != null) {
        LOGGER.debug("process  : @memo this.labelPath = " + this.labelPath);
        msmqMessage.setLabel((String) this.objectMapper.getObjectFromMessage(message, this.labelPath));
      }

      final Object body = this.payloadProxy.getPayload(message);
      // LOGGER.debug("process  : @memo body = " + body);

      if (body == null) {
        throw new ActionProcessingException("No payload");
      }

      String messageBody = null;

      if (body instanceof String) {
        LOGGER.debug("process  : @memo is string");
        messageBody = body.toString();
        msmqMessage.setBinary(false);
      } else if (body instanceof byte[]) {
        LOGGER.debug("process  : @memo is byte[]");
        messageBody = new String(Base64.encodeBase64((byte[]) body));
        msmqMessage.setBinary(true);
      } else {
        LOGGER.debug("process  : @memo is xml");
        final XStream xstream = new XStream();
        messageBody = xstream.toXML(body);

        if (!messageBody.startsWith("<?xml")) {
          messageBody = "<?xml version = \"1.0\" encoding = \"UTF-8\"?>" + messageBody;
        }

        msmqMessage.setBinary(false);
      }

      // LOGGER.debug("process  : @memo messageBody = " + messageBody);
      msmqMessage.setBody(messageBody);
    } catch (final MessageDeliverException e) {
      throw new ActionProcessingException(e);
    } catch (final ObjectMappingException e) {
      throw new ActionProcessingException(e);
    }

    boolean routeMessage = routeMessage(msmqMessage);

    if (!routeMessage) {
      throw new ActionProcessingException("Gateway reported failure");
    }

    return message;
  }

  /**
   * Double-checked Idiom
   * 
   * @param msmqMessage
   * @return
   */
  private boolean routeMessage(final MsmqOutMessage msmqMessage)
  {
    boolean retVal;

    try {
      retVal = this.gateway.routeMessage(this.destination, msmqMessage);
    } catch (final WebServiceException ex) {
      if (WEBSERVICE_NOT_AVAILABLE_ERROR_MSG == null) {
        synchronized (MsmqOutGatewayAction.class) {
          if (WEBSERVICE_NOT_AVAILABLE_ERROR_MSG == null) {
            WEBSERVICE_NOT_AVAILABLE_ERROR_MSG = MessageFormat.format(WEBSERVICE_NOT_AVAILABLE_MSG, ex.getMessage());
            SNMPTRAP_ERROR_APPENDER.error(WEBSERVICE_NOT_AVAILABLE_ERROR_MSG);
          }
        }
      }

      throw ex;
    }

    if (WEBSERVICE_NOT_AVAILABLE_ERROR_MSG != null) {
      synchronized (MsmqOutGatewayAction.class) {
        if (WEBSERVICE_NOT_AVAILABLE_ERROR_MSG != null) {
          SNMPTRAP_CLEAR_APPENDER.info(WEBSERVICE_NOT_AVAILABLE_ERROR_MSG);
          WEBSERVICE_NOT_AVAILABLE_ERROR_MSG = null;
        }
      }
    }

    return retVal;
  }
}
