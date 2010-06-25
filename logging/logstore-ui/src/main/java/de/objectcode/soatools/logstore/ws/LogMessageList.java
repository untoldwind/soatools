package de.objectcode.soatools.logstore.ws;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
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

import de.objectcode.soatools.logstore.persistent.LogMessage;

@Name("logMessageList")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class LogMessageList extends ExtendedDataModel implements Serializable {
	private static final long serialVersionUID = -717019075517069164L;

	LogMessageDetailBean current;
	IFetchCommand fetchCommand;
	int rowCount;

	private Object rowKey;

	private Map<Object, LogMessageBean> mapping;

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
		mapping = null;
	}

	public List<LogMessageBean> loadData(int startRow, int endRow) {

		List<LogMessageBean> result = new ArrayList<LogMessageBean>();

		for (LogMessage logMessage : fetchCommand.retrieve(startRow, endRow,
				logStoreDatabase))
			result.add(new LogMessageBean(logMessage));

		return result;
	}

	/**
	 * Load data range, and iterate over it
	 */
	@Transactional
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {

		SequenceRange sequenceRange = (SequenceRange) range;

		int firstRow = sequenceRange.getFirstRow();
		int rows = sequenceRange.getRows();

		List<LogMessageBean> objects = loadData(firstRow, rows);

		mapping = new HashMap<Object, LogMessageBean>();

		for (int i = 0; i < objects.size(); i++) {
			LogMessageBean data = objects.get(i);
			Object key = data.getId();

			mapping.put(key, data);

			visitor.process(context, key, argument);
		}
	}

	@Override
	public Object getRowKey() {
		return rowKey;
	}

	@Override
	public void setRowKey(Object key) {
		rowKey = key;
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	@Transactional
	public Object getRowData() {
		if (mapping != null && mapping.containsKey(rowKey)) {
			return mapping.get(rowKey);
		} else if (rowKey != null) {
			LogMessage logMessage = (LogMessage) logStoreDatabase.get(
					LogMessage.class, (Long) rowKey);

			if (logMessage != null) {
				LogMessageBean messageBean = new LogMessageBean(logMessage);
				if (mapping == null) {
					mapping = new HashMap<Object, LogMessageBean>();
				}
				mapping.put(rowKey, messageBean);

				return messageBean;
			}
			return null;
		}

		return null;
	}

	@Override
	public int getRowIndex() {
		return -1;
	}

	@Override
	public Object getWrappedData() {
		return null;
	}

	@Override
	public boolean isRowAvailable() {
		return getRowData() != null;
	}

	@Override
	public void setRowIndex(int index) {
	}

	@Override
	public void setWrappedData(Object data) {

	}

	public interface IFetchCommand {
		int getRowCount(Session logStoreSession);

		List<LogMessage> retrieve(int startRow, int rows,
				Session logStoreSession);
	}

	public static abstract class CriteriaFetchCommand implements IFetchCommand {
		public int getRowCount(Session logStoreSession) {
			Criteria criteria = createCriteria(logStoreSession, false);

			criteria.setProjection(Projections.rowCount());

			return (Integer) criteria.uniqueResult();
		}

		@SuppressWarnings("unchecked")
		public List<LogMessage> retrieve(int startRow, int rows,
				Session logStoreSession) {
			Criteria criteria = createCriteria(logStoreSession, true);

			criteria.setFirstResult(startRow);
			criteria.setMaxResults(rows);

			return criteria.list();
		}

		protected abstract Criteria createCriteria(Session logStoreSession,
				boolean order);

	}
}
