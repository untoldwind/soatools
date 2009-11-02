package de.objectcode.soatools.mfm.api;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "message-format-model")
@XmlType(name = "message-format-model")
public class MessageFormatModel
{
  List<ComponentType> types;
  List<MessageFormat> messageFormats;

  @XmlElementRef
  public List<ComponentType> getTypes()
  {
    return types;
  }

  public void setTypes(List<ComponentType> types)
  {
    this.types = types;
  }

  @XmlElementRef
  public List<MessageFormat> getMessageFormats()
  {
    return messageFormats;
  }

  public void setMessageFormats(List<MessageFormat> messageFormats)
  {
    this.messageFormats = messageFormats;
  }

}
