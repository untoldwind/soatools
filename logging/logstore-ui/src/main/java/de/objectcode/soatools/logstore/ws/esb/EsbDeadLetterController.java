package de.objectcode.soatools.logstore.ws.esb;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

import de.objectcode.soatools.logstore.persistent.EsbMessage;


@Name("esbDeadLetterController")
@Scope(ScopeType.CONVERSATION)
public class EsbDeadLetterController implements Serializable
{
  private static final long serialVersionUID = 270626330306892187L;

  private final static Log LOG = LogFactory.getLog(EsbDeadLetterController.class);

  public final static String VIEW_ID = "/secure/esbDeadLetter.xhtml";

  @In
  Session logStoreDatabase;

  @In(required = false)
  @Out
  EsbDeadLetterList esbDeadLetterList;

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
  public String select(EsbDeadLetterBean message)
  {
    EsbMessage deadLetter = (EsbMessage) logStoreDatabase.get(EsbMessage.class, message.getId());

    esbDeadLetterList.setCurrent(new EsbDeadLetterDetailBean(deadLetter));

    return VIEW_ID;
  }

  @Transactional
  public String delete(EsbDeadLetterBean message)
  {
    EsbMessage deadLetter = (EsbMessage) logStoreDatabase.get(EsbMessage.class, message.getId());

    logStoreDatabase.delete(deadLetter);
    logStoreDatabase.flush();

    updateDeadLetterList();
    esbDeadLetterList.setCurrent(null);

    return VIEW_ID;
  }

  private void updateDeadLetterList()
  {
    synchronized (this) {
      Criteria criteria = logStoreDatabase.createCriteria(EsbMessage.class);

      criteria.add(Restrictions.eq("classification", "DLQ"));
      criteria.addOrder(Order.asc("id"));

      esbDeadLetterList = new EsbDeadLetterList();
      esbDeadLetterList.fill(criteria.list());
    }
  }

}
