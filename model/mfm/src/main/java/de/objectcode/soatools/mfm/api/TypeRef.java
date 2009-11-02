package de.objectcode.soatools.mfm.api;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "type-ref")
@XmlType(name = "type-ref")
public class TypeRef extends Type
{
  @Override
  public boolean isTypeRef()
  {
    return true;
  }

  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer("TypeRef(");
    buffer.append("name=").append(name);
    buffer.append(", version=").append(version);
    buffer.append(")");

    return buffer.toString();
  }
}
