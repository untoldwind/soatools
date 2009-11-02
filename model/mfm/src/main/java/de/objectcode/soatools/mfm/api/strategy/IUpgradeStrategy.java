package de.objectcode.soatools.mfm.api.strategy;

import de.objectcode.soatools.mfm.api.Component;
import de.objectcode.soatools.mfm.api.UpgradeException;
import de.objectcode.soatools.mfm.api.accessor.IDataAccessor;
import de.objectcode.soatools.mfm.api.collector.IDataCollector;

/**
 * Strategy to upgrade some hierarchical data to a new message format version.
 * 
 * @author junglas
 */
public interface IUpgradeStrategy {

	/**
	 * Perform an upgrade.
	 * 
	 * @param component
	 *            The component to be upgraded.
	 * @param oldVersion
	 *            Data accessor of the old version
	 * @param newVersion
	 *            Data collector of the new version
	 * @throws UpgradeException
	 */
	void upgradeComponent(Component component, IDataAccessor oldVersion,
			IDataCollector newVersion) throws UpgradeException;
}
