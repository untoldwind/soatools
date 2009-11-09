package de.objectcode.soatools.logstore.impl;

public interface LogStoreServiceMBean {
	long getCurrentPosition();

	String getDataSourceJndiName();

	void setDataSourceJndiName(String dataSourceJndiName);

	String showMessagesByProcessInstanceId(long processInstanceId);

	String showMessagesByProcessInstanceIdAsHtml(long processInstanceId);

	String showMessagesByProcessInstanceIdAsXml(long processInstanceId);

	String showMessagesByTag(String tag, String value, long position);

	String showMessagesByTagXML(String tag, String value, long position);

	int countMessages(long position);

	void start() throws Exception;

	void stop();
}
