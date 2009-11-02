package de.objectcode.soatools.mfm.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Abstract base class of all types that can be used or defined in a message format model.
 * 
 * @author junglas
 */
@XmlType(name = "type-base")
@Entity
@Table(name = "MFM_TYPES")
@Inheritance(strategy = InheritanceType.JOINED)
@IdClass(NameVersionPair.class)
public abstract class Type
{
  String name;
  int version;

  /**
   * Get the name of the type.
   * 
   * @return The name of the type
   */
  @Id
  @XmlAttribute
  @Column(name = "NAME", nullable = false)
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Get the version of the type.
   * 
   * Version numbers should be increased by one on every structural change.
   * 
   * @return The version numbers.
   */
  @Id
  @XmlAttribute
  @Column(name = "VERSION", nullable = false)
  public int getVersion()
  {
    return version;
  }

  public void setVersion(int version)
  {
    this.version = version;
  }

  @XmlTransient
  @Transient
  public boolean isComponentType()
  {
    return false;
  }

  @XmlTransient
  @Transient
  public boolean isValueType()
  {
    return false;
  }

  @XmlTransient
  @Transient
  public boolean isTypeRef()
  {
    return false;
  }

  public void update(IResolveContext context, Type type)
  {
  }

  public void resolve(IResolveContext context)
  {
  }
}
