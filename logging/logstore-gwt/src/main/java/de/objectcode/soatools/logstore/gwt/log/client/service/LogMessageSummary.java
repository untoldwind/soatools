package de.objectcode.soatools.logstore.gwt.log.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.objectcode.soatools.logstore.gwt.utils.client.service.BaseDataPage;

public class LogMessageSummary implements IsSerializable {
	private static final long serialVersionUID = 1L;
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

	public static class Page extends BaseDataPage<LogMessageSummary> {

		public Page() {
		}

		public Page(int pageStart, int pageSize, int totalNumber,
				List<LogMessageSummary> pageData) {
			super(pageStart, pageSize, totalNumber);

			this.pageData = pageData;
		}

		private List<LogMessageSummary> pageData;

		public List<LogMessageSummary> getPageData() {
			return pageData;
		}
	}
}
