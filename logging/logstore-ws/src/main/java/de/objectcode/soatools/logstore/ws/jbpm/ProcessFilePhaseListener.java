package de.objectcode.soatools.logstore.ws.jbpm;

import java.io.IOException;
import java.io.OutputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.command.Command;
import org.jbpm.command.CommandService;
import org.jbpm.command.impl.CommandServiceImpl;
import org.jbpm.graph.def.ProcessDefinition;

public class ProcessFilePhaseListener implements PhaseListener
{
  private static final long serialVersionUID = 5066832480042211352L;

  public void afterPhase(PhaseEvent phaseEvent)
  {
  }

  public void beforePhase(PhaseEvent phaseEvent)
  {
    final FacesContext facesContext = phaseEvent.getFacesContext();
    final ExternalContext externalContext = facesContext.getExternalContext();
    final HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
    final String servletPath = externalContext.getRequestServletPath();
    final String pathInfo = externalContext.getRequestPathInfo();
    String path = servletPath == null ? null : pathInfo == null ? servletPath : servletPath + pathInfo;

    if (path == null || !path.startsWith("/processFile/")) {
      return;
    }

    path = path.substring(13);
    int idx = path.indexOf('/');

    if (idx <= 0) {
      return;
    }

    final long id = Long.parseLong(path.substring(0, idx));
    final String file = path.substring(idx + 1);

    JbpmConfiguration configuration = JbpmConfiguration.getInstance();
    CommandService commandService = new CommandServiceImpl(configuration);

    byte[] bytes = (byte[]) commandService.execute(new Command() {
      private static final long serialVersionUID = 4475421507194917024L;

      public Object execute(JbpmContext jbpmContext) throws Exception
      {
        final ProcessDefinition processDefinition = jbpmContext.getGraphSession().getProcessDefinition(id);
        if (processDefinition == null) {
          try {
            response.sendError(404, "Process definition " + id + " does not exist");
            facesContext.responseComplete();
          } catch (IOException e) {
          }
          return null;
        }
        if (!processDefinition.getFileDefinition().hasFile(file)) {
          try {
            response.sendError(404, "Process definition " + id + " does not contain file '" + file + "'");
            facesContext.responseComplete();
          } catch (IOException e) {
          }

          return null;
        }
        return processDefinition.getFileDefinition().getBytes(file);
      }

    });

    if (bytes == null) {
      return;
    }

    response.setContentLength(bytes.length);

    try {
      final OutputStream outputStream = response.getOutputStream();
      try {
        outputStream.write(bytes);
        outputStream.flush();
      } finally {
        try {
          outputStream.close();
        } catch (IOException e) {
        }
      }
    } catch (IOException e) {
    }

    facesContext.responseComplete();
  }

  public PhaseId getPhaseId()
  {
    return PhaseId.RENDER_RESPONSE;
  }

}
