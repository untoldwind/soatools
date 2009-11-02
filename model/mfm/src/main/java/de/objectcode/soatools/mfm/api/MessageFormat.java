package de.objectcode.soatools.mfm.api;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jboss.soa.esb.message.Message;

import de.objectcode.soatools.mfm.api.accessor.Dom4jDataAccessor;
import de.objectcode.soatools.mfm.api.accessor.MessageBodyDataAccessor;
import de.objectcode.soatools.mfm.api.accessor.StackDataAccessor;
import de.objectcode.soatools.mfm.api.collector.IDataCollector;

@XmlRootElement(name = "message-format")
@XmlType(name = "message-format")
@Entity
@Table(name = "MFM_MESSAGEFORMATS")
@IdClass(NameVersionPair.class)
public class MessageFormat {
	private Set<Component> bodyComponents;
	private String name;
	private int version;

	@XmlElement(name = "body-component", namespace = "http://objectcode.de/soatools/mfm")
	@OneToMany(mappedBy = "ownerMessageBody", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@OrderBy("name")
	public Set<Component> getBodyComponents() {
		return this.bodyComponents;
	}

	@Id
	@XmlAttribute(required = true)
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return this.name;
	}

	@Id
	@XmlAttribute(required = true)
	@Column(name = "VERSION", nullable = false)
	public int getVersion() {
		return this.version;
	}

	public void normalizeMessage(final Message message,
			final IDataCollector collector,
			final IMessageFormatRepository repository)
			throws ValidationException, UpgradeException, DowngradeException {
		collector.setTypeInformation(this.name, this.version);
		if (message.getBody().get() != null
				&& message.getBody().get() instanceof String
				&& ((String) message.getBody().get()).startsWith("<?xml")) {
			try {
				final SAXReader reader = new SAXReader();

				final Document document = reader.read(new StringReader(message
						.getBody().get().toString()));
				final StackDataAccessor accessor = new StackDataAccessor();
				accessor.addComponentAccessor(new Dom4jDataAccessor(document
						.getRootElement()));
				accessor.addComponentAccessor(new MessageBodyDataAccessor(
						message));

				for (final Component component : this.bodyComponents) {
					component.normalize(accessor, collector, repository);
				}
				return;
			} catch (final DocumentException e) {
			}
		}
		final MessageBodyDataAccessor bodyAccessor = new MessageBodyDataAccessor(
				message);

		for (final Component component : this.bodyComponents) {
			component.normalize(bodyAccessor, collector, repository);
		}
	}

	public void resolve(final IResolveContext context) {
		if (this.bodyComponents != null)
			for (final Component component : this.bodyComponents) {
				component.setOwnerMessageBody(this);
				component.resolve(context);
			}
	}

	public void setBodyComponents(final Set<Component> bodyComponents) {
		this.bodyComponents = bodyComponents;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer("MessageFormat(");
		buffer.append("name=").append(this.name);
		buffer.append(", version=").append(this.version);
		buffer.append(", bodyComponents=").append(this.bodyComponents);
		buffer.append(")");

		return buffer.toString();
	}

	public void update(final IResolveContext context,
			final MessageFormat messageFormat) {
		final Set<Component> newComponents = messageFormat.getBodyComponents();

		if (newComponents == null)
			return;

		Iterator<Component> thisCompIter = this.bodyComponents.iterator();
		Iterator<Component> newCompIter = newComponents.iterator();

		while (thisCompIter.hasNext() && newCompIter.hasNext()) {
			final Component thisComp = thisCompIter.next();
			final Component newComp = newCompIter.next();

			thisComp.update(context, newComp);
		}

		while (newCompIter.hasNext()) {
			final Component newComp = newCompIter.next();

			final Component createdComponent = new Component();
			createdComponent.setOwnerMessageBody(this);

			createdComponent.update(context, newComp);

			this.bodyComponents.add(newComp);
		}

		for (int i = newComponents.size(); i < this.bodyComponents.size(); i++) {
			this.bodyComponents.remove(newComponents.size());
		}
	}
}
