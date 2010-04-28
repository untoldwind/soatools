package de.objectcode.soatools.util.healthcheck;

import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

import de.objectcode.soatools.util.healthcheck.check.CheckConfig;
import de.objectcode.soatools.util.healthcheck.check.CheckResult;

public class PerformCheck extends AbstractActionPipelineProcessor {

	CheckConfig checkConfig;

	public PerformCheck(ConfigTree config) throws ConfigurationException {
		try {
			JAXBContext context = JAXBContext.newInstance(CheckConfig.class);

			Unmarshaller unmarshaller = context.createUnmarshaller();

			checkConfig = (CheckConfig) unmarshaller.unmarshal(getClass()
					.getClassLoader().getResourceAsStream(
							config.getRequiredAttribute("check-config")));
		} catch (JAXBException e) {
			throw new ConfigurationException(e);
		}
	}

	public Message process(Message message) throws ActionProcessingException {

		Map<String, CheckResult> results = checkConfig.perform();

		Document document = DocumentFactory.getInstance().createDocument();
		Element root = document.addElement("perform-check");
		
		HealthState state = HealthState.OK;
		
		for ( Map.Entry<String, CheckResult> entry : results.entrySet() ) {
			Element service = root.addElement(entry.getKey());
			
			service.addAttribute("state", entry.getValue().getState().toString());
			
			for ( Map.Entry<String, HealthState> checkEntry : entry.getValue().getCheckStates().entrySet()) {
				service.addElement(checkEntry.getKey()).addText(checkEntry.getValue().toString());
			}
			
			if (entry.getValue().getState().getSeverity() > state.getSeverity() ) {
				state = entry.getValue().getState();
			}
		}
		root.addAttribute("state", state.toString());
		
		message.getBody().add("perform-check", document.asXML());
		
		return message;
	}
}
