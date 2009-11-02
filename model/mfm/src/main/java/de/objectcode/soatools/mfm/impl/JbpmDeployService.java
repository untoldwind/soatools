package de.objectcode.soatools.mfm.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.command.AbstractGetObjectBaseCommand;
import org.jbpm.command.CommandService;
import org.jbpm.command.impl.CommandServiceImpl;
import org.jbpm.graph.def.ProcessDefinition;

public class JbpmDeployService implements JbpmDeployServiceMBean {

	private final static Log LOG = LogFactory.getLog(JbpmDeployService.class);

	String processFiles;

	public String getProcessFiles() {
		return processFiles;
	}

	public void setProcessFiles(String processFiles) {
		this.processFiles = processFiles;
	}

	public void start() throws Exception {
		final JbpmConfiguration configuration = JbpmConfiguration.getInstance();
		CommandService commandService = new CommandServiceImpl(configuration);

		StringTokenizer t = new StringTokenizer(processFiles, ",;: ");
		while (t.hasMoreTokens()) {
			String processFile = t.nextToken();

			try {
				byte[] par = readFile(processFile);

				commandService
						.execute(new DeployProcessWithSigCheckCommand(par));

				LOG.info("Deployed process: " + processFile);
			} catch (IOException e) {
				LOG.error("IOException", e);
			}
		}
	}

	public void stop() {
	}

	private byte[] readFile(String resourceName) throws IOException {
		InputStream in = getClass().getClassLoader().getResourceAsStream(
				resourceName);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int readed;

		while ((readed = in.read(buffer)) > 0) {
			out.write(buffer, 0, readed);
		}
		in.close();
		out.close();

		return out.toByteArray();
	}

	private static class DeployProcessWithSigCheckCommand extends
			AbstractGetObjectBaseCommand {
		private static final long serialVersionUID = 1L;

		private byte[] par;

		public DeployProcessWithSigCheckCommand(byte[] par) {
			this.par = par;
		}

		public Object execute(JbpmContext jbpmContext) throws Exception {
			ZipInputStream zipInputStream = new ZipInputStream(
					new ByteArrayInputStream(par));
			ProcessDefinition processDefinition = ProcessDefinition
					.parseParZipInputStream(zipInputStream);

			if (processDefinition.getFileDefinition().hasFile(
					"processdefinition.xml.sig")) {
				try {
					ProcessDefinition existingProcessDefinition = jbpmContext
							.getGraphSession().findLatestProcessDefinition(
									processDefinition.getName());

					if (existingProcessDefinition.getFileDefinition().hasFile(
							"processdefinition.xml.sig")) {
						if (Arrays.equals(processDefinition.getFileDefinition()
								.getBytes("processdefinition.xml.sig"),
								existingProcessDefinition.getFileDefinition()
										.getBytes("processdefinition.xml.sig"))) {
							// Both signatures exists and are equal
							return retrieveProcessDefinition(existingProcessDefinition);
						}
					}
				} catch (Exception e) {
					// ignore
				}
			}

			jbpmContext.deployProcessDefinition(processDefinition);

			return retrieveProcessDefinition(processDefinition);
		}
	}
}
