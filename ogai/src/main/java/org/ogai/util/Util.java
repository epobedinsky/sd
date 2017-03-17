package org.ogai.util;

import org.apache.commons.lang.time.FastDateFormat;
import org.ogai.core.CommandsRegistry;
import org.ogai.exception.OgaiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Various util routines
 *
 * @author Побединский Евгений
 *         23.03.14 18:14
 */
public class Util {
	public static final Date EMPTY_DATE = new Date();

	public static final String VIEW_DATE_FORMAT = "dd.MM.yyyy";
	public static final String VIEW_DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
	public static final String VIEW_TIME_FORMAT = "HH:mm:ss";
	public static final String JS_VIEW_DATE_FORMAT = "dd.mm.yy";
	public static final String DB_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DB_DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static final String DEFAULT_DATETIME_FORMAT_PATTERN = "yyyy.MM.dd HH:mm:ss.SSS";
	private static final FastDateFormat DEFAULT_DATETIME_FORMAT = FastDateFormat.getInstance(DEFAULT_DATETIME_FORMAT_PATTERN);

	public static boolean isEmpty(String src) {
		return (null == src) || src.isEmpty();
	}

	//TODO переписать
	public static String join(Object[] array, String separator) {
		int len = array.length;
		if (len == 0) {
			return "";
		}
		StringBuilder buf = new StringBuilder(len * 12);
		for (int i = 0; i < len - 1; i++) {
			buf.append(array[i]).append(separator);
		}
		return buf.append(array[len - 1]).toString();
	}

	public static boolean isNotEmpty(String src) {
		return !isEmpty(src);
	}

	public static <T> Set<T> toSet(T... args) {
		Set<T> set = new HashSet<T>();
		for (T arg : args) {
			set.add(arg);
		}

		return set;
	}

	public static boolean isEmpty(Date date) {
		return null == date || EMPTY_DATE.equals(date) || (0 == EMPTY_DATE.compareTo(date));
	}

	public static String formatNow() {
		return formatDateTime(new Date());
	}

	public static String formatDateTime(Date date) {
		return formatDate(date, DEFAULT_DATETIME_FORMAT);
	}

	public static String formatDate(Date date, String dateFormatPattern) {
		assert isNotEmpty(dateFormatPattern);

		return formatDate(date, FastDateFormat.getInstance(dateFormatPattern));
	}

	public static Date toDate(String dateString, String dateFormatPattern) throws ParseException {
		assert isNotEmpty(dateFormatPattern);
		if (isEmpty(dateString)) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatPattern);
		return sdf.parse(dateString); //exception
	}

	private static String formatDate(Date date, FastDateFormat format) {
		if (isEmpty(date)) {
			return "";
		}

		return format.format(date);
	}

	//TODO Это хак. Убрать когда будет введен механизм преобразователей с БД
	public static String toViewDate(String src) throws ParseException {
		if (Util.isEmpty(src)) {
			return src;
		}

		Date date = toDate(src, Util.DB_DATETIME_FORMAT_PATTERN); //exception
		return formatDate(date, Util.VIEW_DATE_FORMAT);
	}

	//TODO Это хак. Убрать когда будет введен механизм преобразователей с БД
	public static String toViewTime(String src) throws ParseException {
		if (Util.isEmpty(src)) {
			return src;
		}

		Date date = toDate(src, Util.DB_DATETIME_FORMAT_PATTERN); //exception
		return formatDate(date, Util.VIEW_TIME_FORMAT);
	}

	//TODO Это хак. Убрать когда будет введен механизм преобразователей с БД
	public static String toViewDateTime(String src) throws ParseException {
		if (Util.isEmpty(src)) {
			return src;
		}

		Date date = toDate(src, Util.DB_DATETIME_FORMAT_PATTERN); //exception
		return formatDate(date, Util.VIEW_DATETIME_FORMAT);
	}

}
