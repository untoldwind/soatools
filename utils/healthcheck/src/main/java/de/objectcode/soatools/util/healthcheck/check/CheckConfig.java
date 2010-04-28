package de.objectcode.soatools.util.healthcheck.check;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.objectcode.soatools.util.healthcheck.HealthState;

@XmlRootElement(name = "check-config")
public class CheckConfig {
	List<Template> templates;
	List<Service> services;

	@XmlElementWrapper(name = "templates")
	@XmlElement(name = "template")
	public List<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}

	@XmlElementWrapper(name = "services")
	@XmlElement(name = "service")
	public List<Service> getChecks() {
		return services;
	}

	public void setChecks(List<Service> services) {
		this.services = services;
	}

	public Map<String, CheckResult> perform() {
		Map<String, Template> templatesByName = new HashMap<String, Template>();

		if (templates != null)
			for (Template template : templates)
				templatesByName.put(template.getName(), template);

		Map<String, CheckResult> results = new LinkedHashMap<String, CheckResult>();

		if (services != null)
			for (Service service : services) {
				Template template = templatesByName.get(service
						.getTemplateName());

				if (template == null) {
					results.put(service.getName(), new CheckResult(HealthState.ERROR, null));
					continue;
				}

				results.put(service.getName(), template.perform(service));
			}
		return results;
	}
}
