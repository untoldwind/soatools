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
	
	public static abstract class Criteria implements IsSerializable {
	}

	public static class ServiceCriteria extends Criteria {
		String serviceCategory;
		String serviceName;

		public String getServiceCategory() {
			return serviceCategory;
		}

		public String getServiceName() {
			return serviceName;
		}
	}
}
