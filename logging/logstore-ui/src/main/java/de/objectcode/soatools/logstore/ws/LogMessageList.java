package de.objectcode.soatools.logstore.ws;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.richfaces.model.ScrollableTableDataModel;
import org.richfaces.model.ScrollableTableDataRange;
import org.richfaces.model.SortOrder;

import de.objectcode.soatools.logstore.persistent.LogMessage;

@Name("logMessageList")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class LogMessageList extends ScrollableTableDataModel<LogMessageBean>
		implements Serializable {
	private static final long serialVersionUID = -717019075517069164L;

	LogMessageDetailBean current;
	IFetchCommand fetchCommand;
	int rowCount;

	@In
	Session logStoreDatabase;

	public LogMessageList() {
	}

	public void setFetchCommand(IFetchCommand fetchCommand) {
		this.fetchCommand = fetchCommand;
	}

	public boolean isHasCurrent() {
		return current != null;
	}

	public LogMessageDetailBean getCurrent() {
		return current;
	}

	public void setCurrent(LogMessageDetailBean current) {
		this.current = current;
	}

	@Transactional
	public void refresh() {
		rowCount = fetchCommand.getRowCount(logStoreDatabase);
	}

	@Override
	@Transactional
	public List<LogMessageBean> loadData(int startRow, int endRow,
			SortOrder sortOrder) {

		List<LogMessageBean> result = new ArrayList<LogMessageBean>();

		for (LogMessage logMessage : fetchCommand.retrieve(startRow, endRow,
				logStoreDatabase))
			result.add(new LogMessageBean(logMessage));

		return result;
	}

	@Transactional
	@Override
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {

		if (range instanceof SequenceRange) {
			SequenceRange sequenceRange = (SequenceRange) range;

			ScrollableTableDataRange newRange = new ScrollableTableDataRange();

			newRange.setFirst(sequenceRange.getFirstRow());
			newRange.setLast(sequenceRange.getFirstRow()
					+ sequenceRange.getRows());
			
			super.walk(context, visitor, newRange, argument);			
		} else
			super.walk(context, visitor, range, argument);
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public Object getWrappedData() {
		return null;
	}

	@Override
	public void setWrappedData(Object data) {
	}

	public interface IFetchCommand {
		int getRowCount(Session logStoreSession);

		List<LogMessage> retrieve(int startRow, int endRow,
				Session logStoreSession);
	}

	public static abstract class CriteriaFetchCommand implements IFetchCommand {
		public int getRowCount(Session logStoreSession) {
			Criteria criteria = createCriteria(logStoreSession, false);

			criteria.setProjection(Projections.rowCount());

			return (Integer) criteria.uniqueResult();
		}

		@SuppressWarnings("unchecked")
		public List<LogMessage> retrieve(int startRow, int endRow,
				Session logStoreSession) {
			Criteria criteria = createCriteria(logStoreSession, true);

			criteria.setFirstResult(startRow);
			criteria.setMaxResults(endRow - startRow);

			return criteria.list();
		}

		protected abstract Criteria createCriteria(Session logStoreSession,
				boolean order);

	}
}
