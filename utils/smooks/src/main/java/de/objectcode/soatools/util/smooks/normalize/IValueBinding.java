package de.objectcode.soatools.util.smooks.normalize;

import org.milyn.container.ExecutionContext;
import org.w3c.dom.Element;

import de.objectcode.soatools.mfm.api.normalize.NormalizedData;

public interface IValueBinding
{
  void applyValue(NormalizedData normalizedData, Element element, ExecutionContext context) throws Exception;
}
