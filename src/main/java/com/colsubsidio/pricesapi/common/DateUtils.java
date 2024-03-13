package com.colsubsidio.pricesapi.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static Date getDate() {
		return new Date();
	}

	public static String getDateString(String formatReturn) {
		var sdf = new SimpleDateFormat(formatReturn);
		return sdf.format(getDate());
	}

	public static Date formatDate(String dateTxt, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(dateTxt);
	}

	public static String toISO(Date date) {
		String ISOFormat = "yyyy-MM-dd'T'HH:mm:ss";
		return new SimpleDateFormat(ISOFormat).format(date);
	}
}
