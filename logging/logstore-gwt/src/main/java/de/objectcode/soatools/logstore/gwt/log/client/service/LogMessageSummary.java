package de.objectcode.soatools.logstore.gwt.log.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LogMessageSummary implements IsSerializable {
	private String id;
	private String serviceCategory;
	private String serviceName;

	public LogMessageSummary() {
	}

	public LogMessageSummary(String id, String serviceCategory,
			String serviceName) {

		this.id = id;
		this.serviceCategory = serviceCategory;
		this.serviceName = serviceName;
	}

	public String getId() {
		return id;
	}

	public String getServiceCategory() {
		return serviceCategory;
	}

	public String getServiceName() {
		return serviceName;
	}

	@Override
	public int hashCode() {
		return ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogMessageSummary other = (LogMessageSummary) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
