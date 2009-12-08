package de.objectcode.soatools.util.smooks.normalize;

import java.util.List;

import org.milyn.container.ExecutionContext;
import org.milyn.javabean.BeanAccessor;
import org.w3c.dom.Element;

import de.objectcode.soatools.mfm.api.accessor.IDataAccessor;
import de.objectcode.soatools.mfm.api.normalize.NormalizedData;

public class RefValueBinding implements IValueBinding
{
  String[] propertyPath;
  String refId;

  public RefValueBinding(String property, String refId) throws Exception
  {
    this.propertyPath = property.split("\\.");

    this.refId = refId;
  }

  public void applyValue(NormalizedData normalizedData, Element element, ExecutionContext context) throws Exception
  {
    Object refObject = BeanAccessor.getBean(refId, context);

    if (refObject == null || !(refObject instanceof NormalizedData)) {
      return;
    }

    for (int i = 0; i < propertyPath.length; i++) {
      String property = propertyPath[i];
      int offset = property.indexOf('[');

      if (offset > 0) {
        int end = property.indexOf(']', offset + 1);
        int index = Integer.parseInt(property.substring(offset + 1, end));

        property = property.substring(0, offset);
        List<IDataAccessor> array = normalizedData.getComponentArray(property);

        if (array == null || array.size() <= index) {
          normalizedData = (NormalizedData) normalizedData.addToComponentArray(property, index);
        } else {
          normalizedData = (NormalizedData) array.get(index);
        }
      } else {
        NormalizedData component = (NormalizedData) normalizedData.getComponent(propertyPath[i]);

        if (component == null) {
          component = (NormalizedData) normalizedData.addComponent(propertyPath[i]);
        }

        normalizedData = component;
      }
    }

    ((NormalizedData) refObject).marshal(normalizedData);
  }
}
