package com.colsubsidio.pricesapi.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	public static Date getDate() {
		return new Date();
	}

	public static String getDateString(String formatReturn) {
		try {
			var sdf = new SimpleDateFormat(formatReturn);
			return sdf.format(getDate());
		} catch (Exception e) {
			var error = new StringBuilder();
			error.append(DateUtils.class.getName());
			error.append(";");
			error.append("formatDate");
			error.append(";");
			error.append(e.getMessage());
			return "";
		}
	}

	public static Date formatDate(String dateTxt, String format) {
		try {
			return new SimpleDateFormat(format).parse(dateTxt);
		} catch (Exception e) {
			var error = new StringBuilder();
			error.append(DateUtils.class.getName());
			error.append(";");
			error.append("formatDate");
			error.append(";");
			error.append(e.getMessage());
			return null;
		}
	}

	public static String toISO(Date date) {
		String ISOFormat = "yyyy-MM-dd'T'HH:mm:ss";
		return new SimpleDateFormat(ISOFormat).format(date);
	}
}
