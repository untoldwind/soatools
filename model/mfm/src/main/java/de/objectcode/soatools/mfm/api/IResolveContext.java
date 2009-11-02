package de.objectcode.soatools.mfm.api;

public interface IResolveContext
{
  Type merge(Type type);

  MessageFormat merge(MessageFormat messageFormat);
}
