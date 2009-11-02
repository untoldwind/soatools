package de.objectcode.soatools.mfm.impl;

public interface MessageFormatRepositoryServiceMBean
{
  void start() throws Exception;

  void stop();

  public String getDataSourceJndiName();

  public void setDataSourceJndiName(String dataSourceJndiName);

  public String getDialect();

  public void setDialect(String dialect);

  public String showMessageFormats() throws Exception;

  public String getMessageFormatResources();

  public void setMessageFormatResources(String messageFormatResources);
}
