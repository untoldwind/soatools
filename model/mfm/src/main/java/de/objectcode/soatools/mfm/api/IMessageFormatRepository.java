package de.objectcode.soatools.mfm.api;

import java.util.Collection;

public interface IMessageFormatRepository
{
  String JNDI_NAME = "messageFormatRepository";

  MessageFormat getMessageFormat(String name, int version);

  ComponentType getComplexType(String name, int version);

  void registerModel(MessageFormatModel messageFormatModel);

  Collection<MessageFormat> getMessageFormats();
}
