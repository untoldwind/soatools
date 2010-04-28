package de.objectcode.soatools.util.healthcheck.check;

import java.util.Map;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="data-provider")
public abstract class DataProvider {
	public abstract Map<String, Object> retrieveData(Service service) throws Exception;
}
