package de.objectcode.soatools.util.smooks.normalize;

import java.util.List;

import org.milyn.container.ExecutionContext;
import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;
import org.w3c.dom.Element;

import de.objectcode.soatools.mfm.api.accessor.IDataAccessor;
import de.objectcode.soatools.mfm.api.normalize.NormalizedData;

abstract class AbstractValueBinding implements IValueBinding
{
  String[] propertyPath;
  DataDecoder decoder;
  String typeAlias;

  AbstractValueBinding(String property, String typeAlias)
  {
    if (typeAlias == null) {
      typeAlias = "String";
    }
    this.propertyPath = property.split("\\.");
    this.typeAlias = typeAlias;

  }

  public void applyValue(NormalizedData normalizedData, Element element, ExecutionContext context) throws Exception
  {
    String value = getValue(element);

    if (value == null) {
      return;
    }

    if (propertyPath.length > 1) {
      for (int i = 0; i < propertyPath.length - 1; i++) {
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
    }

    if (decoder == null) {
      decoder = getDecoder(context);
    }
    normalizedData.addValue(propertyPath[propertyPath.length - 1], decoder.decode(value));
  }

  abstract String getValue(Element element) throws Exception;

  private DataDecoder getDecoder(ExecutionContext executionContext) throws DataDecodeException
  {
    List<?> decoders = executionContext.getDeliveryConfig().getObjects("decoder:" + typeAlias);

    if (decoders == null || decoders.isEmpty()) {
      decoder = DataDecoder.Factory.create(typeAlias);
    } else if (!(decoders.get(0) instanceof DataDecoder)) {
      throw new DataDecodeException("Configured decoder '" + typeAlias + ":" + decoders.get(0).getClass().getName()
          + "' is not an instance of " + DataDecoder.class.getName());
    } else {
      decoder = (DataDecoder) decoders.get(0);
    }

    return decoder;
  }
}
