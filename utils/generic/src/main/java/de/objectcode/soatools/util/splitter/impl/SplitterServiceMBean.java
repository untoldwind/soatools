package de.objectcode.soatools.util.splitter.impl;

public interface SplitterServiceMBean {

	String getDataSourceJndiName();

	void setDataSourceJndiName(String dataSourceJndiName);
	
	void start() throws Exception;

	void stop();
}
