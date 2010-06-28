package de.objectcode.soatools.logstore.gwt.utils.client.service;

import java.util.List;

public interface IDataPage<T> {
	public int getTotalNumber();

	public int getPageNumber();


	public int getPageSize();


	public List<T> getPageData();
	
}
