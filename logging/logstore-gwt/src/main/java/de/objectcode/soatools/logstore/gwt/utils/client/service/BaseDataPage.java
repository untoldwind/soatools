package de.objectcode.soatools.logstore.gwt.utils.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class BaseDataPage<T> implements IDataPage<T>, IsSerializable {
	protected int pageStart;
	protected int pageSize;
	protected int totalNumber;

	protected BaseDataPage() {
	}

	protected BaseDataPage(int pageStart, int pageSize, int totalNumber) {
		this.pageStart = pageStart;
		this.pageSize = pageSize;
		this.totalNumber = totalNumber;
	}

	public int getPageStart() {
		return pageStart;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

}
