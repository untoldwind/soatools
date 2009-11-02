package de.objectcode.soatools.mfm.api.accessor;

import java.util.ArrayList;
import java.util.List;

public class StackDataAccessor implements IDataAccessor
{
  List<IDataAccessor> dataAccessors = new ArrayList<IDataAccessor>();

  public void addComponentAccessor(IDataAccessor componentAccessor)
  {
    dataAccessors.add(componentAccessor);
  }

  public List<Object> getArray(String name)
  {
    for (IDataAccessor componentAccessor : dataAccessors) {
      List<Object> result = componentAccessor.getArray(name);

      if (result != null) {
        return result;
      }
    }

    return null;
  }

  public IDataAccessor getComponent(String name)
  {
    for (IDataAccessor componentAccessor : dataAccessors) {
      IDataAccessor result = componentAccessor.getComponent(name);

      if (result != null) {
        return result;
      }
    }

    return null;
  }

  public List<IDataAccessor> getComponentArray(String name)
  {
    for (IDataAccessor componentAccessor : dataAccessors) {
      List<IDataAccessor> result = componentAccessor.getComponentArray(name);

      if (result != null) {
        return result;
      }
    }
    return null;
  }

  public Object getValue(String name)
  {
    for (IDataAccessor componentAccessor : dataAccessors) {
      Object result = componentAccessor.getValue(name);

      if (result != null) {
        return result;
      }
    }

    return null;
  }

  public String getType()
  {
    for (IDataAccessor componentAccessor : dataAccessors) {
      if (componentAccessor.getType() != null) {
        return componentAccessor.getType();
      }
    }
    return null;
  }

  public int getVersion()
  {
    for (IDataAccessor componentAccessor : dataAccessors) {
      if (componentAccessor.getType() != null) {
        return componentAccessor.getVersion();
      }
    }

    return 0;
  }

}
