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
 * Just drop a component on up- or downgrade.
 * 
 * @author junglas
 */
@XmlType(name = "drop-strategy")
@Entity
@DiscriminatorValue("D")
public class DropStrategy extends StrategyBase implements IUpgradeStrategy,
		IDowngradeStrategy {
	/**
	 * {@inheritDoc}
	 */
	public void downgradeComponent(Component component,
			IDataCollector oldVersion, IDataAccessor newVersion)
			throws DowngradeException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void upgradeComponent(Component component, IDataAccessor oldVersion,
			IDataCollector newVersion) throws UpgradeException {
	}
}
