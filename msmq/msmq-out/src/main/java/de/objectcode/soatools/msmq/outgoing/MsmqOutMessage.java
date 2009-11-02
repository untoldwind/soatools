package de.objectcode.soatools.msmq.outgoing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MsmqOutMessage complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;MsmqOutMessage&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;all&gt;
 *         &lt;element name=&quot;body&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;isXml&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot;/&gt;
 *         &lt;element name=&quot;label&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;correlationId&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MsmqOutMessage", propOrder = {

})
public class MsmqOutMessage
{
  @XmlElement(required = true)
  protected String body;
  protected boolean binary;
  @XmlElement(required = true)
  protected String label;
  @XmlElement(required = true)
  protected String correlationId;

  /**
   * Gets the value of the body property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getBody()
  {
    return body;
  }

  /**
   * Sets the value of the body property.
   * 
   * @param value
   *                allowed object is {@link String }
   * 
   */
  public void setBody(String value)
  {
    this.body = value;
  }

  /**
   * Gets the value of the isXml property.
   * 
   */
  public boolean isBinary()
  {
    return binary;
  }

  /**
   * Sets the value of the isXml property.
   * 
   */
  public void setBinary(boolean value)
  {
    this.binary = value;
  }

  /**
   * Gets the value of the label property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getLabel()
  {
    return label;
  }

  /**
   * Sets the value of the label property.
   * 
   * @param value
   *                allowed object is {@link String }
   * 
   */
  public void setLabel(String value)
  {
    this.label = value;
  }

  /**
   * Gets the value of the correlationId property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCorrelationId()
  {
    return correlationId;
  }

  /**
   * Sets the value of the correlationId property.
   * 
   * @param value
   *                allowed object is {@link String }
   * 
   */
  public void setCorrelationId(String value)
  {
    this.correlationId = value;
  }

}
