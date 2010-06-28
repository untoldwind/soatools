package de.objectcode.soatools.logstore.gwt.utils.client.ui;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.TimeZone;

public interface IFormatter {
	String format(Object value);

	public static class IntegerFormatter implements IFormatter {
		NumberFormat format;

		public IntegerFormatter(NumberFormat format) {
			this.format = format;
		}

		public String format(Object value) {
			return format.format((Integer) value);
		}
	}

	public static class LongFormatter implements IFormatter {
		NumberFormat format;

		public LongFormatter(NumberFormat format) {
			this.format = format;
		}

		public String format(Object value) {
			return format.format((Long) value);
		}
	}

	public static class DoubleFormatter implements IFormatter {
		NumberFormat format;

		public DoubleFormatter(NumberFormat format) {
			this.format = format;
		}

		public String format(Object value) {
			return format.format((Double) value);
		}
	}

	public static class DateTimeFormatter implements IFormatter {
		DateTimeFormat format;
		TimeZone timezone;

		public DateTimeFormatter(DateTimeFormat format) {
			this(format, null);
		}

		public DateTimeFormatter(DateTimeFormat format, TimeZone timezone) {
			this.format = format;
			this.timezone = timezone;
		}

		public String format(Object value) {
			if (timezone != null)
				return format.format((Date) value, timezone);
			return format.format((Date) value);
		}
	}
}
