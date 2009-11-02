package de.objectcode.soatools.logstore.ws;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("logStoreLogDetail")
@Scope(ScopeType.CONVERSATION)
public class LogStoreLogDetailController implements Serializable
{
  private static final long serialVersionUID = -11013439034283885L;

  public final static String VIEW_ID = "/secure/logDetail.xhtml";

  @In
  LogMessageList logMessageList;

  String selectedDetailTab;

  public String select(LogMessageBean logMessage)
  {
    logMessageList.setCurrent(logMessage);

    return VIEW_ID;
  }

  public String getSelectedDetailTab()
  {
    return selectedDetailTab;
  }

  public void setSelectedDetailTab(String selectedDetailTab)
  {
    this.selectedDetailTab = selectedDetailTab;
  }

  @End
  public String close()
  {
    return LogStoreSearchController.VIEW_ID;
  }
}
