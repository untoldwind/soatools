package de.objectcode.soatools.logstore.ws;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("navisionIn")
@Scope(ScopeType.CONVERSATION)
public class NavisionInController implements Serializable
{
  private static final long serialVersionUID = 836579279974125969L;

  private final static Log LOG = LogFactory.getLog(NavisionInController.class);

  public final static String VIEW_ID = "/secure/navision-in.xhtml";

  private String message;

  private QueueConnectionFactory queueConnectionFactory;
  private Queue queue;

  public NavisionInController()
  {
    try {
      final InitialContext iniCtx = new InitialContext();

      final Object tmp = iniCtx.lookup("ConnectionFactory");
      queueConnectionFactory = (QueueConnectionFactory) tmp;
      this.queue = (Queue) iniCtx.lookup("queue/navision_in_request_gw");
    } catch (Exception e) {
      LOG.error("Exception", e);
    }
  }

  @End
  public String enter()
  {
    return VIEW_ID;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  public String send()
  {
    System.out.println(">>> sending: " + message);
    QueueConnection connection = null;
    QueueSession session = null;

    try {
      connection = queueConnectionFactory.createQueueConnection();
      session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      connection.start();

      MessageProducer producer = session.createProducer(queue);

      producer.send(session.createTextMessage(message));
      producer.close();
    } catch (Exception e) {
      LOG.error("Exception", e);
    } finally {
      if (session != null) {
        try {
          session.close();
        } catch (JMSException e) {
        }
      }
      if (connection != null) {
        try {
          connection.stop();
          connection.close();
        } catch (JMSException e) {
        }
      }

    }
    return VIEW_ID;
  }
}
