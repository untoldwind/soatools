package de.objectcode.soatools.util.healthcheck.check;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.soatools.util.healthcheck.HealthState;

@XmlType(name = "template")
public class Template {
	private final static Log LOGGER = LogFactory.getLog(Template.class);

	String name;
	List<DataProvider> dataProviders;
	List<Check> checks;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElementWrapper(name = "data-providers")
	@XmlElements( { @XmlElement(name = "mbean-data-provider", type = MBeanDataProvider.class) })
	public List<DataProvider> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(List<DataProvider> dataProviders) {
		this.dataProviders = dataProviders;
	}

	@XmlElementWrapper(name = "checks")
	@XmlElement(name = "check")
	public List<Check> getChecks() {
		return checks;
	}

	public void setChecks(List<Check> checks) {
		this.checks = checks;
	}

	public CheckResult perform(Service service) {
		try {
			HealthState serviceState = HealthState.OK;
			Map<String, HealthState> checkStates = new LinkedHashMap<String, HealthState>();

			Map<String, Object> values = new HashMap<String, Object>();

			if (dataProviders != null)
				for (DataProvider dataProvider : dataProviders)
					values.putAll(dataProvider.retrieveData(service));


			if ( checks != null ) {
				for ( Check check : checks ) {
					HealthState checkState = check.perform(service, values);
					
					checkStates.put(check.getName(), checkState);
					
					if ( checkState.getSeverity() > serviceState.getSeverity())
						serviceState = checkState;
				}
			}
			return new CheckResult(serviceState, checkStates);
		} catch (Exception e) {
			LOGGER.error("Exception", e);

			return new CheckResult(HealthState.ERROR, null);
		}
	}
}
