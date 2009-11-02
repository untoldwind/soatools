package de.objectcode.soatools.mfm.api.strategy;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.soatools.mfm.api.Component;
import de.objectcode.soatools.mfm.api.DowngradeException;
import de.objectcode.soatools.mfm.api.UpgradeException;
import de.objectcode.soatools.mfm.api.ValueType;
import de.objectcode.soatools.mfm.api.accessor.IDataAccessor;
import de.objectcode.soatools.mfm.api.collector.IDataCollector;

/**
 * Set a fixed value on a component upon up- or downgrade.
 * 
 * @author junglas
 */
@XmlType(name = "set-value-strategy")
@Entity
@DiscriminatorValue("V")
public class SetValueStrategy extends StrategyBase implements IUpgradeStrategy,
		IDowngradeStrategy {
	String valueStr;

	@XmlAttribute(name = "value")
	@Column(name = "VALUE_STR")
	public String getValueStr() {
		return valueStr;
	}

	public void setValueStr(String valueStr) {
		this.valueStr = valueStr;
	}

	/**
	 * {@inheritDoc}
	 */
	public void upgradeComponent(Component component, IDataAccessor oldVersion,
			IDataCollector newVersion) throws UpgradeException {
		if (component.getType().isValueType()) {
			Object value = null;

			try {
				value = ((ValueType) component.getType())
						.valueFromString(valueStr);
			} catch (Exception e) {
			}

			if (value == null) {
				throw new UpgradeException("Invalid value '" + valueStr + "'");
			}

			newVersion.addValue(component.getName(), value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void downgradeComponent(Component component,
			IDataCollector oldVersion, IDataAccessor newVersion)
			throws DowngradeException {
		if (component.getType().isValueType()) {
			Object value = null;

			try {
				value = ((ValueType) component.getType())
						.valueFromString(valueStr);
			} catch (Exception e) {
			}

			if (value == null) {
				throw new DowngradeException("Invalid value '" + valueStr + "'");
			}

			oldVersion.addValue(component.getName(), value);
		}
	}

}
