package de.objectcode.soatools.logstore.ws.jbm;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

import de.objectcode.soatools.logstore.persistent.LogJmsDeadLetter;


@Name("jbmDeadLetterController")
@Scope(ScopeType.CONVERSATION)
public class JbmDLQController implements Serializable
{
  private static final long serialVersionUID = 270626330306892187L;

  private final static Log LOG = LogFactory.getLog(JbmDLQController.class);

  public final static String VIEW_ID = "/secure/jbmDeadLetter.xhtml";

  @In
  Session logStoreDatabase;

  @In(required = false)
  @Out
  JbmDeadLetterList jbmDeadLetterList;

  @Begin(join = true)
  @Transactional
  public String enter()
  {
    updateDeadLetterList();

    return VIEW_ID;
  }

  @Transactional
  public String refresh()
  {
    updateDeadLetterList();

    return VIEW_ID;
  }

  @Transactional
  public String select(JbmDeadLetterBean message)
  {
    LogJmsDeadLetter deadLetter = (LogJmsDeadLetter) logStoreDatabase.get(LogJmsDeadLetter.class, message.getId());

    jbmDeadLetterList.setCurrent(new JbmDeadLetterDetailBean(deadLetter));

    return VIEW_ID;
  }

  @Transactional
  public String delete(JbmDeadLetterBean message)
  {
    LogJmsDeadLetter deadLetter = (LogJmsDeadLetter) logStoreDatabase.get(LogJmsDeadLetter.class, message.getId());

    logStoreDatabase.delete(deadLetter);
    logStoreDatabase.flush();

    updateDeadLetterList();
    jbmDeadLetterList.setCurrent(null);

    return VIEW_ID;
  }

  private void updateDeadLetterList()
  {
    synchronized (this) {
      Criteria criteria = logStoreDatabase.createCriteria(LogJmsDeadLetter.class);

      criteria.addOrder(Order.asc("id"));

      jbmDeadLetterList = new JbmDeadLetterList();
      jbmDeadLetterList.fill(criteria.list());
    }
  }
}
