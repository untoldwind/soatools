package de.objectcode.soatools.logstore.gwt.utils.client.ui;

import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DefaultCalendarView;
import com.google.gwt.user.datepicker.client.DefaultMonthSelector;

public class ExtendedDatePicker extends DatePicker {
	public ExtendedDatePicker() {
	    super(new DefaultMonthSelector(), new DefaultCalendarView(),
	            new ExtendedCalendarModel());
	}
}
