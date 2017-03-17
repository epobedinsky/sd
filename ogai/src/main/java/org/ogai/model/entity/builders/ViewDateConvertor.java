package org.ogai.model.entity.builders;

import org.apache.commons.lang.time.FastDateFormat;
import org.ogai.exception.OgaiException;
import org.ogai.util.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Конвертор дат
 *
 * @author Побединский Евгений
 *         18.04.14 1:15
 */
public class ViewDateConvertor implements ViewEntityConvertor<Date> {
	@Override
	public Date toEntity(String viewValue) throws OgaiException {
		if (Util.isEmpty(viewValue)) {
			return null;
		}

		try {
			return Util.toDate(viewValue, Util.VIEW_DATE_FORMAT); //exception
		} catch (ParseException e) {
			throw new OgaiException("Error parsing date:" + viewValue + ", format:" + Util.VIEW_DATE_FORMAT, e);
		}
	}
}
