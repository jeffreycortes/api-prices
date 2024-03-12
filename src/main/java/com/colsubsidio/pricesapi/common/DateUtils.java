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

	public static String formatDate(Date date, String formatReturn) {
		try {
			var sdf = new SimpleDateFormat(formatReturn);
			return sdf.format(date);
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

	public static String toISO(Date date) {
		String ISOFormat = "yyyy-MM-dd'T'HH:mm:ss";
		return new SimpleDateFormat(ISOFormat).format(date);
	}

	/* "yyyy-MM-dd":"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" */
	public static String formatDate(String format, String dateTxt, String formatReturn) {
		try {
			var date = new SimpleDateFormat(format, Locale.ENGLISH).parse(dateTxt);
			var sdf = new SimpleDateFormat(formatReturn);
			return sdf.format(date);
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

	public static String addSubtractMontsDate(int months, String formatReturn) {
		try {
			var date = addSubtractMontsDate(months);
			var sdf = new SimpleDateFormat(formatReturn);
			return sdf.format(date);
		} catch (Exception e) {
			var error = new StringBuilder();
			error.append(DateUtils.class.getName());
			error.append(";");
			error.append("addSubtractDaysDate");
			error.append(";");
			error.append(e.getMessage());
			return "";
		}
	}

	public static Date addSubtractMontsDate(int months) {
		try {
			var calendar = Calendar.getInstance();
			calendar.setTime(getDate());
			calendar.add(Calendar.MONTH, -months);
			return calendar.getTime();
		} catch (Exception e) {
			var error = new StringBuilder();
			error.append(DateUtils.class.getName());
			error.append(";");
			error.append("addSubtractMontsDate");
			error.append(";");
			error.append(e.getMessage());
			return null;
		}
	}

}
