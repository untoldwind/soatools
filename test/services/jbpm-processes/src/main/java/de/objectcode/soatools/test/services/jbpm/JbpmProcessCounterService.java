package de.objectcode.soatools.test.services.jbpm;

import java.util.List;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;

public class JbpmProcessCounterService implements
		JbpmProcessCounterServiceMBean {

	public void start() throws Exception {
	}

	public void stop() {
	}

	public int countProcessInstances(String processName) {
		JbpmConfiguration configuration = JbpmConfiguration.getInstance();

		JbpmContext context = configuration.createJbpmContext();

		try {
			ProcessDefinition definition = context.getGraphSession()
					.findLatestProcessDefinition(processName);

			return context.getGraphSession().findProcessInstances(
					definition.getId()).size();
		} finally {
			context.close();
		}
	}

	@SuppressWarnings("unchecked")
	public int countFinishedProcessInstances(String processName) {
		JbpmConfiguration configuration = JbpmConfiguration.getInstance();

		JbpmContext context = configuration.createJbpmContext();

		try {
			ProcessDefinition definition = context.getGraphSession()
					.findLatestProcessDefinition(processName);

			int count = 0;
			for (ProcessInstance processInstance : (List<ProcessInstance>) context
					.getGraphSession().findProcessInstances(definition.getId())) {
				if (processInstance.hasEnded()) {
					count++;
				}
			}
			return count;
		} finally {
			context.close();
		}
	}
}
