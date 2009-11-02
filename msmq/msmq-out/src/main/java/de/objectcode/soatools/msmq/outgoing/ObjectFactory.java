
package de.objectcode.soatools.msmq.outgoing;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the at.liwest.msmq.outgoing package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MsmqOutMessage_QNAME = new QName("http://liwest.at/msmq/outgoing", "MsmqOutMessage");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: at.liwest.msmq.outgoing
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MsmqOutMessage }
     * 
     */
    public MsmqOutMessage createMsmqOutMessage() {
        return new MsmqOutMessage();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MsmqOutMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://liwest.at/msmq/outgoing", name = "MsmqOutMessage")
    public JAXBElement<MsmqOutMessage> createMsmqOutMessage(MsmqOutMessage value) {
        return new JAXBElement<MsmqOutMessage>(_MsmqOutMessage_QNAME, MsmqOutMessage.class, null, value);
    }

}
