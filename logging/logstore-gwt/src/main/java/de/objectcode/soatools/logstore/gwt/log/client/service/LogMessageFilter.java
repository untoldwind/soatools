package de.objectcode.soatools.logstore.gwt.log.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LogMessageFilter implements IsSerializable {
	List<Criteria> criterias;

	public enum CriteriaType {
		TAGVALUE,
		SERVICE,
		TIMESTAMP
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
	}
}
