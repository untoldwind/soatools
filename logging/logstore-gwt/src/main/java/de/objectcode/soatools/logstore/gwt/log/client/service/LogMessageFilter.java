package de.objectcode.soatools.logstore.gwt.log.client.service;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LogMessageFilter implements IsSerializable {
	List<Criteria> criterias;

	public enum CriteriaType {
		TAGVALUE, SERVICE, TIMESTAMP
	}

	public LogMessageFilter() {
	}

	public LogMessageFilter(List<Criteria> criterias) {
		this.criterias = criterias;
	}

	public List<Criteria> getCriterias() {
		return criterias;
	}

	public static abstract class Criteria implements IsSerializable {
		public abstract CriteriaType getType();
	}

	public static class ServiceCriteria extends Criteria {
		String serviceCategory;
		String serviceName;

		public ServiceCriteria() {
		}

		public ServiceCriteria(String serviceCategory, String serviceName) {
			this.serviceCategory = serviceCategory;
			this.serviceName = serviceName;
		}

		public String getServiceCategory() {
			return serviceCategory;
		}

		public String getServiceName() {
			return serviceName;
		}

		@Override
		public CriteriaType getType() {
			return CriteriaType.SERVICE;
		}
	}

	public static class TagCriteria extends Criteria {
		String tagName;
		String tagValue;

		public TagCriteria() {
		}

		public TagCriteria(String tagName, String tagValue) {
			this.tagName = tagName;
			this.tagValue = tagValue;
		}

		public String getTagName() {
			return tagName;
		}

		public String getTagValue() {
			return tagValue;
		}

		@Override
		public CriteriaType getType() {
			return CriteriaType.TAGVALUE;
		}
	}
	
	public static class TimestampCriteria extends Criteria {
		Date from;
		Date until;
		

		public TimestampCriteria() {			
		}
		
		public TimestampCriteria(Date from, Date until) {
			this.from = from;
			this.until = until;
		}


		public Date getFrom() {
			return from;
		}

		public Date getUntil() {
			return until;
		}

		@Override
		public CriteriaType getType() {
			return CriteriaType.TIMESTAMP;
		}
		
	}
}
