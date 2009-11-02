package de.objectcode.soatools.logstore.ws.jbpm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.command.Command;
import org.jbpm.command.CommandService;
import org.jbpm.command.impl.CommandServiceImpl;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Name("processInformation")
@Scope(ScopeType.CONVERSATION)
public class ProcessInformation implements Serializable
{
  private static final long serialVersionUID = -8346984956208379605L;

  public DiagramInfo getDiagramInfo(final long processInstanceId)
  {
    JbpmConfiguration configuration = JbpmConfiguration.getInstance();
    CommandService commandService = new CommandServiceImpl(configuration);

    return (DiagramInfo) commandService.execute(new Command() {
      private static final long serialVersionUID = 1L;

      public Object execute(JbpmContext jbpmContext) throws Exception
      {
        final ProcessInstance processInstance = jbpmContext.getGraphSession().getProcessInstance(processInstanceId);
        final ProcessDefinition processDefinition = processInstance.getProcessDefinition();
        final Map<Long, String> nodeNames = new HashMap<Long, String>();

        if (processDefinition == null) {
          return null;
        }

        for (Object obj : processDefinition.getNodes()) {
          org.jbpm.graph.def.Node node = (org.jbpm.graph.def.Node) obj;
          nodeNames.put(node.getId(), node.getName());
        }

        Document document = XmlUtil
            .parseXmlInputStream(processDefinition.getFileDefinition().getInputStream("gpd.xml"));
        Element processDiagramElement = document.getDocumentElement();
        final String widthString = processDiagramElement.getAttribute("width");
        final String heightString = processDiagramElement.getAttribute("height");
        final List<DiagramNodeInfo> diagramNodeInfoList = new ArrayList<DiagramNodeInfo>();
        final NodeList nodeNodeList = processDiagramElement.getElementsByTagName("node");
        final int nodeNodeListLength = nodeNodeList.getLength();
        for (int i = 0; i < nodeNodeListLength; i++) {
          final Node nodeNode = nodeNodeList.item(i);
          if (nodeNode instanceof Node && nodeNode.getParentNode() == processDiagramElement) {
            final Element nodeElement = (Element) nodeNode;
            final String nodeName = nodeElement.getAttribute("name");
            final String nodeXString = nodeElement.getAttribute("x");
            final String nodeYString = nodeElement.getAttribute("y");
            final String nodeWidthString = nodeElement.getAttribute("width");
            final String nodeHeightString = nodeElement.getAttribute("height");
            final DiagramNodeInfo nodeInfo = new DiagramNodeInfo(nodeName, Integer.parseInt(nodeXString), Integer
                .parseInt(nodeYString), Integer.parseInt(nodeWidthString), Integer.parseInt(nodeHeightString));
            diagramNodeInfoList.add(nodeInfo);
          }
        }

        return new DiagramInfo(processDefinition.getId(), processDefinition.getName(), processDefinition.getVersion(),
            Integer.parseInt(heightString), Integer.parseInt(widthString), nodeNames, diagramNodeInfoList);
      }
    });
  }

  public static final class DiagramInfo implements Serializable
  {
    private static final long serialVersionUID = 1L;

    private final long processDefinitionId;
    private final String processDefinitionName;
    private final int processDefinitionVersion;
    private final int width;
    private final int height;
    private final Map<Long, String> nodeNames;
    private final Map<String, DiagramNodeInfo> nodeMap;

    public DiagramInfo(final long processDefinitionId, final String processDefinitionName,
        final int processDefinitionVersion, final int height, final int width, final Map<Long, String> nodeNames,
        final List<DiagramNodeInfo> nodeList)
    {
      this.processDefinitionId = processDefinitionId;
      this.processDefinitionName = processDefinitionName;
      this.processDefinitionVersion = processDefinitionVersion;
      this.height = height;
      this.width = width;
      final LinkedHashMap<String, DiagramNodeInfo> map = new LinkedHashMap<String, DiagramNodeInfo>();
      for (DiagramNodeInfo nodeInfo : nodeList) {
        map.put(nodeInfo.getName(), nodeInfo);
      }
      this.nodeNames = Collections.unmodifiableMap(nodeNames);
      nodeMap = Collections.unmodifiableMap(map);
    }

    public long getProcessDefinitionId()
    {
      return processDefinitionId;
    }

    public String getProcessDefinitionName()
    {
      return processDefinitionName;
    }

    public int getProcessDefinitionVersion()
    {
      return processDefinitionVersion;
    }

    public int getHeight()
    {
      return height;
    }

    public Map<String, DiagramNodeInfo> getNodeMap()
    {
      return nodeMap;
    }

    public Map<Long, String> getNodeNames()
    {
      return nodeNames;
    }

    public List<DiagramNodeInfo> getNodes()
    {
      return Collections.unmodifiableList(new ArrayList<DiagramNodeInfo>(nodeMap.values()));
    }

    public int getWidth()
    {
      return width;
    }
  }

  public static final class DiagramNodeInfo implements Serializable
  {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public DiagramNodeInfo(final String name, final int x, final int y, final int width, final int height)
    {
      this.height = height;
      this.name = name;
      this.width = width;
      this.x = x;
      this.y = y;
    }

    public int getHeight()
    {
      return height;
    }

    public String getName()
    {
      return name;
    }

    public int getWidth()
    {
      return width;
    }

    public int getX()
    {
      return x;
    }

    public int getY()
    {
      return y;
    }
  }

}
