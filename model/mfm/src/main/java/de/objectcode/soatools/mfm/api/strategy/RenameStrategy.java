package de.objectcode.soatools.mfm.api.strategy;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.soatools.mfm.api.Component;
import de.objectcode.soatools.mfm.api.DowngradeException;
import de.objectcode.soatools.mfm.api.UpgradeException;
import de.objectcode.soatools.mfm.api.accessor.IDataAccessor;
import de.objectcode.soatools.mfm.api.collector.IDataCollector;

/**
 * Rename a component on up- and downgrade.
 * 
 * @author junglas
 */
@XmlType(name = "rename-strategy")
@Entity
@DiscriminatorValue("R")
public class RenameStrategy extends TransferStrategyBase implements
		IUpgradeStrategy, IDowngradeStrategy {
	String oldName;

	@XmlAttribute(name = "old-name")
	@Column(name = "OLD_NAME")
	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void upgradeComponent(Component component, IDataAccessor oldVersion,
			IDataCollector newVersion) throws UpgradeException {
		transferUp(component.getType(), component.isArray(), oldVersion,
				oldName, newVersion, component.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	public void downgradeComponent(Component component,
			IDataCollector oldVersion, IDataAccessor newVersion)
			throws DowngradeException {
		transferDown(component.getType(), component.isArray(), newVersion,
				component.getName(), oldVersion, oldName);
	}

}
