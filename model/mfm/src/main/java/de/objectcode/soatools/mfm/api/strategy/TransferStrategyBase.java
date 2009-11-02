package de.objectcode.soatools.mfm.api.strategy;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlTransient;

import de.objectcode.soatools.mfm.api.ComponentType;
import de.objectcode.soatools.mfm.api.DowngradeException;
import de.objectcode.soatools.mfm.api.Type;
import de.objectcode.soatools.mfm.api.UpgradeException;
import de.objectcode.soatools.mfm.api.accessor.IDataAccessor;
import de.objectcode.soatools.mfm.api.collector.IDataCollector;

/**
 * Base class of all up- and downgrade strategies doing a simple data transfer
 * from old to new version or vice versa.
 * 
 * @author junglas
 */
@XmlTransient
@Entity
@DiscriminatorValue("1")
public abstract class TransferStrategyBase extends StrategyBase {
	protected void transferUp(Type type, boolean array, IDataAccessor source,
			String sourceName, IDataCollector target, String targetName)
			throws UpgradeException {
		if (type.isComponentType()) {
			ComponentType componentType = (ComponentType) type;

			if (array) {
				List<IDataAccessor> componentArray = source
						.getComponentArray(sourceName);

				if (componentArray != null) {
					for (int i = 0; i < componentArray.size(); i++) {
						IDataCollector element = target.addToComponentArray(
								targetName, i);

						componentType.upgradeType(componentArray.get(i),
								element);
					}
				}
			} else {
				IDataAccessor componentData = source.getComponent(sourceName);
				IDataCollector targetComponent = target
						.addComponent(targetName);

				componentType.upgradeType(componentData, targetComponent);
			}
		} else if (type.isValueType()) {
			if (array) {
				List<Object> valueArray = source.getArray(sourceName);

				if (valueArray != null) {
					for (int i = 0; i < valueArray.size(); i++) {
						target.addToArray(targetName, i, valueArray.get(i));
					}
				}
			} else {
				Object value = source.getValue(sourceName);

				if (value != null) {
					target.addValue(targetName, value);
				}
			}
		}
	}

	protected void transferDown(Type type, boolean array, IDataAccessor source,
			String sourceName, IDataCollector target, String targetName)
			throws DowngradeException {
		if (type.isComponentType()) {
			ComponentType componentType = (ComponentType) type;

			if (array) {
				List<IDataAccessor> componentArray = source
						.getComponentArray(sourceName);

				if (componentArray != null) {
					for (int i = 0; i < componentArray.size(); i++) {
						IDataCollector element = target.addToComponentArray(
								targetName, i);

						componentType.downgradeType(element, componentArray
								.get(i));
					}
				}
			} else {
				IDataAccessor componentData = source.getComponent(sourceName);
				IDataCollector targetComponent = target
						.addComponent(targetName);

				componentType.downgradeType(targetComponent, componentData);
			}
		} else if (type.isValueType()) {
			if (array) {
				List<Object> valueArray = source.getArray(sourceName);

				if (valueArray != null) {
					for (int i = 0; i < valueArray.size(); i++) {
						target.addToArray(targetName, i, valueArray.get(i));
					}
				}
			} else {
				Object value = source.getValue(sourceName);

				if (value != null) {
					target.addValue(targetName, value);
				}
			}
		}
	}
}
