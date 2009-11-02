package de.objectcode.soatools.mfm.api.strategy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.soatools.mfm.api.Component;
import de.objectcode.soatools.mfm.api.DowngradeException;
import de.objectcode.soatools.mfm.api.UpgradeException;
import de.objectcode.soatools.mfm.api.accessor.IDataAccessor;
import de.objectcode.soatools.mfm.api.collector.IDataCollector;

/**
 * Just keep a component on up- or downgrade.
 * 
 * @author junglas
 */
@XmlType(name = "keep-strategy")
@Entity
@DiscriminatorValue("K")
public class KeepStrategy extends TransferStrategyBase implements
		IUpgradeStrategy, IDowngradeStrategy {
	/**
	 * {@inheritDoc}
	 */
	public void downgradeComponent(Component component,
			IDataCollector oldVersion, IDataAccessor newVersion)
			throws DowngradeException {
		transferDown(component.getType(), component.isArray(), newVersion,
				component.getName(), oldVersion, component.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	public void upgradeComponent(Component component, IDataAccessor oldVersion,
			IDataCollector newVersion) throws UpgradeException {
		transferUp(component.getType(), component.isArray(), oldVersion,
				component.getName(), newVersion, component.getName());
	}
}
