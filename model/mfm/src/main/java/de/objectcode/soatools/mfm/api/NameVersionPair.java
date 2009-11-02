package de.objectcode.soatools.mfm.api;

import java.io.Serializable;

public class NameVersionPair implements Serializable
{
  private static final long serialVersionUID = 804113248239790891L;

  String name;
  int version;

  public NameVersionPair()
  {
  }

  public NameVersionPair(String name, int version)
  {
    this.name = name;
    this.version = version;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public int getVersion()
  {
    return version;
  }

  public void setVersion(int version)
  {
    this.version = version;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof NameVersionPair)) {
      return false;
    }

    NameVersionPair castObj = (NameVersionPair) obj;

    return name.equals(castObj.name) && version == castObj.version;
  }

  @Override
  public int hashCode()
  {
    int hash = name.hashCode();

    hash = 13 * hash + version;

    return hash;
  }

  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer("NameVersionPair(");
    buffer.append("name=").append(name);
    buffer.append(", version=").append(version);

    return buffer.toString();
  }

}
