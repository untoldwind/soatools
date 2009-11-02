package de.objectcode.soatools.mfm.api.strategy;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.apache.bsf.BSFManager;
import org.mvel.MVEL;

import de.objectcode.soatools.mfm.api.Component;
import de.objectcode.soatools.mfm.api.DowngradeException;
import de.objectcode.soatools.mfm.api.UpgradeException;
import de.objectcode.soatools.mfm.api.accessor.IDataAccessor;
import de.objectcode.soatools.mfm.api.collector.IDataCollector;

/**
 * Execute a MVEL expression upon up- or downgrade.
 * 
 * @author junglas
 */
@XmlType(name = "script-expression-strategy")
@Entity
@DiscriminatorValue("S")
public class ScriptExpressionStrategy extends StrategyBase implements
		IDowngradeStrategy, IUpgradeStrategy {
	String language;
	String script;
	String oldName;

	@XmlAttribute(name = "old-name")
	@Column(name = "OLD_NAME")
	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	@XmlAttribute
	@Column(name = "SCRIPT_LANGUAGE")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@XmlValue
	@Lob
	@Column(name = "SCRIPT")
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * {@inheritDoc}
	 */
	public void upgradeComponent(Component component, IDataAccessor oldVersion,
			IDataCollector newVersion) throws UpgradeException {
		Object value = null;

		// Workaround for missing MVEL integration in SOA
		if ("MVEL".equalsIgnoreCase(language)) {
			Map<String, Object> vars = new HashMap<String, Object>();

			vars.put("oldVersion", oldVersion);

			try {
				value = MVEL.eval(script, vars);
			} catch (Exception e) {
				throw new UpgradeException(e);
			}
		} else {

			BSFManager manager = new BSFManager();

			manager.registerBean("oldVersion", oldVersion);

			try {
				value = manager.eval(language == null ? "MVEL" : language, "",
						1, 1, script);
			} catch (Exception e) {
				throw new UpgradeException(e);
			}
		}

		if (value != null) {
			newVersion.addValue(component.getName(), value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void downgradeComponent(Component component,
			IDataCollector oldVersion, IDataAccessor newVersion)
			throws DowngradeException {
		Object value = null;

		// Workaround for missing MVEL integration in SOA
		if ("MVEL".equalsIgnoreCase(language)) {
			Map<String, Object> vars = new HashMap<String, Object>();

			vars.put("newVersion", newVersion);

			try {
				value = MVEL.eval(script, vars);
			} catch (Exception e) {
				throw new DowngradeException(e);
			}
		} else {
			BSFManager manager = new BSFManager();

			manager.registerBean("newVersion", newVersion);

			try {
				value = manager.eval(language == null ? "beanshell" : language,
						"", 1, 1, script);
			} catch (Exception e) {
				throw new DowngradeException(e);
			}
		}

		if (value != null) {
			oldVersion.addValue(
					oldName != null ? oldName : component.getName(), value);
		}
	}
}
