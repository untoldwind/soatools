package de.objectcode.soatools.util.smooks.normalize;

import org.w3c.dom.Element;

public class StaticValueBinding extends AbstractValueBinding
{
  String value;

  public StaticValueBinding(String property, String typeAlias, String value) throws Exception
  {
    super(property, typeAlias);

    this.value = value;
  }

  @Override
  String getValue(Element element) throws Exception
  {
    return value;
  }

}
