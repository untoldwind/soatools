package de.objectcode.soatools.mfm.api;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import de.objectcode.soatools.mfm.api.accessor.IDataAccessor;
import de.objectcode.soatools.mfm.api.collector.IDataCollector;
import de.objectcode.soatools.mfm.api.strategy.DropStrategy;
import de.objectcode.soatools.mfm.api.strategy.IDowngradeStrategy;
import de.objectcode.soatools.mfm.api.strategy.IUpgradeStrategy;
import de.objectcode.soatools.mfm.api.strategy.KeepStrategy;
import de.objectcode.soatools.mfm.api.strategy.RenameStrategy;
import de.objectcode.soatools.mfm.api.strategy.ScriptExpressionStrategy;
import de.objectcode.soatools.mfm.api.strategy.SetValueStrategy;
import de.objectcode.soatools.mfm.api.strategy.StrategyBase;


/**
 * A Component is part of a ComponentType or a MessageFormat.
 * 
 * @author junglas
 * 
 */
@Entity
@Table(name = "MFM_COMPONENTS")
@XmlType(name = "component")
public class Component
{
  long id;
  String name;
  Type type;
  boolean required;
  boolean array;
  IDowngradeStrategy downgradeStrategy;
  IUpgradeStrategy upgradeStrategy;
  ComponentType ownerType;
  MessageFormat ownerMessageBody;

  @Id
  @GeneratedValue(generator = "SEQ_MFM_COMPONENTS")
  @GenericGenerator(name = "SEQ_MFM_COMPONENTS", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_MFM_COMPONENTS"))
  @XmlTransient
  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  @XmlAttribute(required = true)
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @XmlElementRef
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumns( { @JoinColumn(name = "TYPE_NAME", referencedColumnName = "NAME"),
      @JoinColumn(name = "TYPE_VERSION", referencedColumnName = "VERSION") })
  public Type getType()
  {
    return type;
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  @XmlAttribute(required = false)
  @Column(name = "ISREQUIRED", nullable = false)
  @org.hibernate.annotations.Type(type = "yes_no")
  public boolean isRequired()
  {
    return required;
  }

  public void setRequired(boolean required)
  {
    this.required = required;
  }

  @XmlAttribute(required = false)
  @Column(name = "ISARRAY", nullable = false)
  @org.hibernate.annotations.Type(type = "yes_no")
  public boolean isArray()
  {
    return array;
  }

  public void setArray(boolean array)
  {
    this.array = array;
  }

  @XmlElements( {
      @XmlElement(name = "downgrade-keep", namespace = "http://objectcode.de/soatools/mfm", type = KeepStrategy.class),
      @XmlElement(name = "downgrade-rename", namespace = "http://objectcode.de/soatools/mfm", type = RenameStrategy.class),
      @XmlElement(name = "downgrade-expression", namespace = "http://objectcode.de/soatools/mfm", type = ScriptExpressionStrategy.class),
      @XmlElement(name = "downgrade-set-value", namespace = "http://objectcode.de/soatools/mfm", type = SetValueStrategy.class),
      @XmlElement(name = "downgrade-drop", namespace = "http://objectcode.de/soatools/mfm", type = DropStrategy.class) })
  @ManyToOne(targetEntity = StrategyBase.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "DOWNGRADE_STRATEGY_ID")
  public IDowngradeStrategy getDowngradeStrategy() throws DowngradeException
  {
    return downgradeStrategy;
  }

  public void setDowngradeStrategy(IDowngradeStrategy downgradeStrategy)
  {
    this.downgradeStrategy = downgradeStrategy;
  }

  @XmlElements( {
      @XmlElement(name = "upgrade-keep", namespace = "http://objectcode.de/soatools/mfm", type = KeepStrategy.class),
      @XmlElement(name = "upgrade-rename", namespace = "http://objectcode.de/soatools/mfm", type = RenameStrategy.class),
      @XmlElement(name = "upgrade-expression", namespace = "http://objectcode.de/soatools/mfm", type = ScriptExpressionStrategy.class),
      @XmlElement(name = "upgrade-set-value", namespace = "http://objectcode.de/soatools/mfm", type = SetValueStrategy.class),
      @XmlElement(name = "upgrade-drop", namespace = "http://objectcode.de/soatools/mfm", type = DropStrategy.class) })
  @ManyToOne(targetEntity = StrategyBase.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "UPGRADE_STRATEGY_ID")
  public IUpgradeStrategy getUpgradeStrategy() throws UpgradeException
  {
    return upgradeStrategy;
  }

  public void setUpgradeStrategy(IUpgradeStrategy upgradeStrategy)
  {
    this.upgradeStrategy = upgradeStrategy;
  }

  @XmlTransient
  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "OWNER_TYPE_NAME", referencedColumnName = "NAME"),
      @JoinColumn(name = "OWNER_TYPE_VERSION", referencedColumnName = "VERSION") })
  public ComponentType getOwnerType()
  {
    return ownerType;
  }

  public void setOwnerType(ComponentType ownerType)
  {
    this.ownerType = ownerType;
  }

  @XmlTransient
  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "OWNER_MESSAGEBODY_NAME", referencedColumnName = "NAME"),
      @JoinColumn(name = "OWNER_MESSAGEBODY_VERSION", referencedColumnName = "VERSION") })
  public MessageFormat getOwnerMessageBody()
  {
    return ownerMessageBody;
  }

  public void setOwnerMessageBody(MessageFormat ownerMessageBody)
  {
    this.ownerMessageBody = ownerMessageBody;
  }

  public void validate(IDataAccessor dataAccessor) throws ValidationException
  {
    if (type.isComponentType()) {
      ComponentType componentType = (ComponentType) type;

      if (array) {
        List<IDataAccessor> componentArray = dataAccessor.getComponentArray(name);

        if (required && (componentArray == null || componentArray.isEmpty())) {
          throw new ValidationException(name + " is a required component");
        } else if (componentArray != null) {
          for (IDataAccessor componentData : componentArray) {
            componentType.validate(componentData);
          }
        }
      } else {
        IDataAccessor componentData = dataAccessor.getComponent(name);

        if (componentData == null && required) {
          throw new ValidationException(name + " is a required component");
        } else if (componentData != null) {
          componentType.validate(componentData);
        }
      }
    } else if (type.isValueType()) {
      ValueType valueType = (ValueType) type;

      if (array) {
        List<Object> valueArray = dataAccessor.getArray(name);

        if (required && (valueArray == null || valueArray.isEmpty())) {
          throw new ValidationException(name + " is a required component");
        } else if (valueArray != null) {
          for (Object value : valueArray) {
            valueType.validateValue(value);
          }
        }
      } else {
        Object value = dataAccessor.getValue(name);

        if (value == null && required) {
          throw new ValidationException(name + " is a required component");
        } else if (value != null) {
          valueType.validateValue(value);
        }
      }
    }
  }

  public void normalize(IDataAccessor dataAccessor, IDataCollector collector, IMessageFormatRepository repository)
      throws ValidationException, UpgradeException, DowngradeException
  {
    if (type.isComponentType()) {
      ComponentType componentType = (ComponentType) type;

      if (array) {
        List<IDataAccessor> componentArray = dataAccessor.getComponentArray(name);

        if (required && (componentArray == null || componentArray.isEmpty())) {
          throw new ValidationException(name + " is a required component");
        } else if (componentArray != null) {
          for (int i = 0; i < componentArray.size(); i++) {
            IDataAccessor componentData = componentArray.get(i);
            IDataCollector elementCollector = collector.addToComponentArray(name, i);

            componentType.normalize(componentData, elementCollector, repository);
          }
        }
      } else {
        IDataAccessor componentData = dataAccessor.getComponent(name);

        if (componentData == null && required) {
          throw new ValidationException(name + " is a required component");
        } else if (componentData != null) {
          componentType.validate(componentData);
          IDataCollector elementCollector = collector.addComponent(name);

          componentType.normalize(componentData, elementCollector, repository);
        }
      }
    } else if (type.isValueType()) {
      ValueType valueType = (ValueType) type;

      if (array) {
        List<Object> valueArray = dataAccessor.getArray(name);

        if (required && (valueArray == null || valueArray.isEmpty())) {
          throw new ValidationException(name + " is a required component");
        } else if (valueArray != null) {
          for (int i = 0; i < valueArray.size(); i++) {
            Object value = valueArray.get(i);
            value = valueType.normalizeValue(value);
            collector.addToArray(name, i, value);
          }
        }
      } else {
        Object value = dataAccessor.getValue(name);

        if (value == null && required) {
          throw new ValidationException(name + " is a required component");
        } else if (value != null) {
          value = valueType.normalizeValue(value);
          collector.addValue(name, value);
        }
      }
    }
  }

  public void update(IResolveContext context, Component component)
  {
    this.name = component.getName();
    this.array = component.isArray();
    this.required = component.isRequired();
    this.type = component.getType();

    resolve(context);
  }

  public void resolve(IResolveContext context)
  {
    if (type.getName() == null) {
      if (ownerType != null) {
        type.setName(ownerType.getName() + "." + name);
        type.setVersion(ownerType.getVersion());
      }
      if (ownerMessageBody != null) {
        type.setName(ownerMessageBody.getName() + ".body." + name);
        type.setVersion(ownerMessageBody.getVersion());
      }
    }

    type = context.merge(type);
    type.resolve(context);

    if (upgradeStrategy == null) {
      upgradeStrategy = new KeepStrategy();
    }
    if (downgradeStrategy == null) {
      downgradeStrategy = new KeepStrategy();
    }
  }

  public void upgradeComponent(IDataAccessor oldVersion, IDataCollector newVersion) throws UpgradeException
  {
    upgradeStrategy.upgradeComponent(this, oldVersion, newVersion);
  }

  public void downgradeComponent(IDataCollector oldVersion, IDataAccessor newVersion) throws DowngradeException
  {
    downgradeStrategy.downgradeComponent(this, oldVersion, newVersion);
  }

  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer("Component(");
    buffer.append("name=").append(name);
    buffer.append(", type=").append(type);
    buffer.append(", required=").append(required);
    buffer.append(", array=").append(array);
    buffer.append(")");

    return buffer.toString();
  }
}
