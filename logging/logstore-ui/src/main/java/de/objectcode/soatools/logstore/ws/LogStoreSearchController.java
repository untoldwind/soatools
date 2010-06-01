package de.objectcode.soatools.logstore.ws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

import de.objectcode.soatools.logstore.persistent.LogMessage;
import de.objectcode.soatools.logstore.persistent.LogTag;

@Name("logStoreSearch")
@Scope(ScopeType.CONVERSATION)
public class LogStoreSearchController implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String VIEW_ID = "/secure/main.xhtml";

	private String processInstanceId;
	private String tagName;
	private String tagValue;
	private Date fromDate;
	private Date untilDate;

	@In
	Session logStoreDatabase;

	@Out(required = false)
	LogMessageList logMessageList;

	@End
	public String enter() {
		return VIEW_ID;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Begin
	@Transactional
	public String findByProcessInstanceId() {
		logMessageList = new LogMessageList(new LogMessageList.IRefreshCommand() {
			public void refresh(LogMessageList list, Session logStoreSession) {
				final Criteria criteria = logStoreSession
						.createCriteria(LogMessage.class);

				criteria.add(Restrictions.eq("jbpmProcessInstanceId", Long
						.parseLong(processInstanceId)));
				criteria.addOrder(Order.asc("id"));

				logMessageList.fill(criteria.list());
			}
		});
		logMessageList.refresh(logStoreDatabase);

		return LogStoreLogDetailController.VIEW_ID;
	}

	@Begin
	@Transactional
	public String findByTagValue() {
		logMessageList = new LogMessageList(new LogMessageList.IRefreshCommand() {
			public void refresh(LogMessageList list, Session logStoreSession) {
				final Criteria criteria = logStoreSession
						.createCriteria(LogTag.class);

				criteria.setProjection(Projections.property("logMessage"));
				criteria.add(Restrictions.and(Restrictions.eq("name", tagName),
						Restrictions.eq("tagValue", tagValue)));
				Criteria logMessageCriteria = criteria
						.createCriteria("logMessage");
				if (fromDate != null) {
					logMessageCriteria.add(Restrictions.ge("logEnterTimestamp",
							fromDate));
				}
				if (untilDate != null) {
					logMessageCriteria.add(Restrictions.le("logEnterTimestamp",
							untilDate));
				}
				criteria.addOrder(Order.asc("logMessage.id"));

				logMessageList.fill(criteria.list());
			}
		});
		logMessageList.refresh(logStoreDatabase);

		return LogStoreLogDetailController.VIEW_ID;

	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getUntilDate() {
		return untilDate;
	}

	public void setUntilDate(Date untilDate) {
		this.untilDate = untilDate;
	}

	@Transactional
	public List<SelectItem> getTagNames() {
		final Criteria criteria = logStoreDatabase.createCriteria(LogTag.class);
		criteria.setProjection(Projections.distinct(Projections
				.property("name")));

		List<SelectItem> items = new ArrayList<SelectItem>();

		for (Object name : criteria.list()) {
			items.add(new SelectItem(name.toString(), name.toString()));
		}

		return items;
	}
}
