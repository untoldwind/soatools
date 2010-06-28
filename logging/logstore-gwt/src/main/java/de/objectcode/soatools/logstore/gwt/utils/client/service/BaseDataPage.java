package de.objectcode.soatools.logstore.gwt.utils.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class BaseDataPage<T> implements IDataPage<T>, IsSerializable {
	protected int pageNumber;
	protected int pageSize;
	protected int totalNumber;

	protected BaseDataPage() {
	}

	protected BaseDataPage(int pageNumber, int pageSize, int totalNumber) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalNumber = totalNumber;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

}
