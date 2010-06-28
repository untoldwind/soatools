package de.objectcode.soatools.logstore.gwt.utils.client.ui;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarModel;

public class ExtendedCalendarModel extends CalendarModel {

	@Override
	protected DateTimeFormat getDayOfWeekFormatter() {
	    return DateTimeFormat.getFormat("ccc");
	}

}
