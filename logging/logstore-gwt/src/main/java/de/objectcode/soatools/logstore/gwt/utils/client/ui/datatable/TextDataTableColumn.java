package de.objectcode.soatools.logstore.gwt.utils.client.ui.datatable;

import de.objectcode.soatools.logstore.gwt.utils.client.service.IProjection;
import de.objectcode.soatools.logstore.gwt.utils.client.ui.IFormatter;

public class TextDataTableColumn<RowClass> extends DataTableColumn<RowClass> {

	public TextDataTableColumn(String header, String width) {
		super(header, width, null, null);
	}

	public TextDataTableColumn(String header, String width,
			IProjection<RowClass> projection) {
		super(header, width, projection, null);
	}

	public TextDataTableColumn(String header, String width,
			IProjection<RowClass> projection, IFormatter formatter) {
		super(header, width, projection, formatter);
	}

	@Override
	public boolean isWidget() {
		return false;
	}

	public String getCellText(RowClass row) {
		Object value = row != null && projection != null ? projection.project(row) : row;

		if ( value == null )
			return "";
		
		return formatter != null ? formatter.format(value) : value.toString();
	}
}
