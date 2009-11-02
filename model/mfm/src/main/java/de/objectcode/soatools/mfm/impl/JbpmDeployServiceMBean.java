package de.objectcode.soatools.mfm.impl;

public interface JbpmDeployServiceMBean {
	void start() throws Exception;

	void stop();

	String getProcessFiles();

	void setProcessFiles(String processFiles);
}
