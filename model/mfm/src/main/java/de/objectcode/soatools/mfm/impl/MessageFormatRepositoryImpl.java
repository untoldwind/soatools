package de.objectcode.soatools.mfm.impl;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import de.objectcode.soatools.mfm.api.ComponentType;
import de.objectcode.soatools.mfm.api.IMessageFormatRepository;
import de.objectcode.soatools.mfm.api.MessageFormat;
import de.objectcode.soatools.mfm.api.MessageFormatModel;
import de.objectcode.soatools.mfm.api.NameVersionPair;


public class MessageFormatRepositoryImpl implements IMessageFormatRepository
{
  SessionFactory sessionFactory;

  public MessageFormatRepositoryImpl(SessionFactory sessionFactory)
  {
    this.sessionFactory = sessionFactory;
  }

  public MessageFormat getMessageFormat(String name, int version)
  {
    Session session = null;

    try {
      session = sessionFactory.openSession();

      return (MessageFormat) session.get(MessageFormat.class, new NameVersionPair(name, version));
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  public ComponentType getComplexType(String name, int version)
  {
    Session session = null;

    try {
      session = sessionFactory.openSession();

      return (ComponentType) session.get(ComponentType.class, new NameVersionPair(name, version));
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  public synchronized void registerModel(MessageFormatModel messageFormatModel)
  {
    Session session = null;

    try {
      session = sessionFactory.openSession();

      ResolveContextImpl context = new ResolveContextImpl(this, session);

      if (messageFormatModel.getTypes() != null) {
        for (ComponentType componentType : messageFormatModel.getTypes()) {
          context.merge(componentType);
        }
      }
      if (messageFormatModel.getTypes() != null) {
        for (ComponentType componentType : messageFormatModel.getTypes()) {
          componentType.resolve(context);
        }
      }
      if (messageFormatModel.getMessageFormats() != null) {
        for (MessageFormat messageFormat : messageFormatModel.getMessageFormats()) {
          context.merge(messageFormat);
          messageFormat.resolve(context);
        }
      }

      session.flush();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  @SuppressWarnings("unchecked")
  public Collection<MessageFormat> getMessageFormats()
  {
    Session session = null;

    try {
      session = sessionFactory.openSession();

      Query query = session.createQuery("select o from " + MessageFormat.class.getName()
          + " as o order by o.name asc, o.version asc");

      return query.list();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }
}
