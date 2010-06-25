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
	private String serviceCategory;
	private String serviceName;
	private Date fromDate;
	private Date untilDate;

	@In
	Session logStoreDatabase;

	@In("logMessageList")
	@Out("logMessageList")
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
	public String findByProcessInstanceId() {
		logMessageList
				.setFetchCommand(new LogMessageList.CriteriaFetchCommand() {
					@Override
					protected Criteria createCriteria(Session logStoreSession,
							boolean order) {
						final Criteria criteria = logStoreSession
								.createCriteria(LogMessage.class);

						criteria.add(Restrictions.eq("jbpmProcessInstanceId",
								Long.parseLong(processInstanceId)));
						if (order)
							criteria.addOrder(Order.asc("id"));

						return criteria;
					}
				});

		logMessageList.refresh();

		return LogStoreLogDetailController.VIEW_ID;
	}

	@Begin
	public String findByTagValue() {
		logMessageList
				.setFetchCommand(new LogMessageList.CriteriaFetchCommand() {
					@Override
					protected Criteria createCriteria(Session logStoreSession,
							boolean order) {
						final Criteria criteria = logStoreSession
								.createCriteria(LogTag.class);

						criteria.setProjection(Projections
								.property("logMessage"));
						if (tagValue != null && tagValue.indexOf('*') > 0)
							criteria.add(Restrictions.and(Restrictions.eq(
									"name", tagName), Restrictions.like(
									"tagValue", tagValue.replace('*', '%'))));
						else
							criteria.add(Restrictions.and(Restrictions.eq(
									"name", tagName), Restrictions.eq(
									"tagValue", tagValue)));
						Criteria logMessageCriteria = criteria
								.createCriteria("logMessage");
						if (fromDate != null) {
							logMessageCriteria.add(Restrictions.ge(
									"logEnterTimestamp", fromDate));
						}
						if (untilDate != null) {
							logMessageCriteria.add(Restrictions.le(
									"logEnterTimestamp", untilDate));
						}
						if (order)
							criteria.addOrder(Order.asc("logMessage.id"));

						return criteria;
					}
				});

		logMessageList.refresh();

		return LogStoreLogDetailController.VIEW_ID;
	}

	@Begin
	public String findByServiceName() {
		logMessageList
				.setFetchCommand(new LogMessageList.CriteriaFetchCommand() {
					@Override
					protected Criteria createCriteria(Session logStoreSession,
							boolean order) {
						final Criteria criteria = logStoreSession
								.createCriteria(LogMessage.class);

						if (serviceCategory != null
								&& serviceCategory.length() > 0) {
							if (serviceCategory.indexOf('*') > 0)
								criteria.add(Restrictions.like(
										"serviceCategory", serviceCategory
												.replace('*', '%')));
							else
								criteria.add(Restrictions.eq("serviceCategory",
										serviceCategory));
						}
						if (serviceName != null && serviceName.length() > 0) {
							if (serviceName.indexOf('*') > 0)
								criteria.add(Restrictions.like("serviceName",
										serviceName.replace('*', '%')));
							else
								criteria.add(Restrictions.eq("serviceName",
										serviceName));
						}
						if (fromDate != null) {
							criteria.add(Restrictions.ge("logEnterTimestamp",
									fromDate));
						}
						if (untilDate != null) {
							criteria.add(Restrictions.le("logEnterTimestamp",
									untilDate));
						}
						if (order)
							criteria.addOrder(Order.asc("id"));

						return criteria;
					}
				});

		logMessageList.refresh();

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

	public String getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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
