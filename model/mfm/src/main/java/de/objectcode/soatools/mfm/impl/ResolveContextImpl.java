package de.objectcode.soatools.mfm.impl;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import de.objectcode.soatools.mfm.api.IResolveContext;
import de.objectcode.soatools.mfm.api.MessageFormat;
import de.objectcode.soatools.mfm.api.NameVersionPair;
import de.objectcode.soatools.mfm.api.Type;


public class ResolveContextImpl implements IResolveContext
{
  Session session;
  Set<Type> visited = new HashSet<Type>();

  public ResolveContextImpl(MessageFormatRepositoryImpl repository, Session session)
  {
    this.session = session;
  }

  public Type merge(Type type)
  {
    if (visited.contains(type)) {
      return type;
    }

    NameVersionPair key = new NameVersionPair(type.getName(), type.getVersion());

    if (type.isTypeRef()) {
      Type referencedType = (Type) session.get(Type.class, key);

      visited.add(referencedType);

      return referencedType;
    } else {
      Type existing = (Type) session.get(Type.class, key);

      if (existing != null) {
        visited.add(existing);
        existing.update(this, type);

        return existing;
      }

      visited.add(type);
      type.resolve(this);

      session.persist(type);

      return type;
    }
  }

  public MessageFormat merge(MessageFormat messageFormat)
  {
    NameVersionPair key = new NameVersionPair(messageFormat.getName(), messageFormat.getVersion());

    MessageFormat existing = (MessageFormat) session.get(MessageFormat.class, key);

    if (existing != null) {
      existing.update(this, messageFormat);

      return existing;
    }
    messageFormat.resolve(this);

    session.persist(messageFormat);

    return messageFormat;
  }
}
