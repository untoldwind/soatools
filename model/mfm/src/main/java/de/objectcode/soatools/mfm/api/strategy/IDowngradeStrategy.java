package de.objectcode.soatools.mfm.api.strategy;

import de.objectcode.soatools.mfm.api.Component;
import de.objectcode.soatools.mfm.api.DowngradeException;
import de.objectcode.soatools.mfm.api.accessor.IDataAccessor;
import de.objectcode.soatools.mfm.api.collector.IDataCollector;

/**
 * Strategy to downgrade some hierarchical data to an older message format
 * version.
 * 
 * @author junglas
 */
public interface IDowngradeStrategy {
	/**
	 * Perform a downgrade.
	 * 
	 * @param component
	 *            The data component to be downgraded
	 * @param oldVersion
	 *            Data collector of the old version
	 * @param newVersion
	 *            Data accessor of the new version
	 * @throws DowngradeException
	 *             on error
	 */
	void downgradeComponent(Component component, IDataCollector oldVersion,
			IDataAccessor newVersion) throws DowngradeException;
}
