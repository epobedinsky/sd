package org.ogai.grid.columns;

import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.util.Util;

import java.util.Date;

/**
 * Колонка отображающая дату в некотором формате
 *
 * @author Побединский Евгений
 *         18.04.14 22:17
 */
public class DateColumn extends GridColumn{
	private String formatString;

	/**
	 * @param name Не пустое имя столбца. Именно с таким именем значение из результатов запроса пойдет в грид
	 * @param formatString - строка формата в соответствии с которой нужно отформатировать дату
	 */
	public DateColumn(String name, String formatString) {
		super(name);
		this.formatString = formatString;
	}

	/**
	 * @param name Не пустое имя столбца. Именно с таким именем значение из результатов запроса пойдет в грид
	 *             Столбец будет форматировать даты в соответсвии с дефолтным форматом отображения
	 */
	public DateColumn(String name) {
		this(name, Util.VIEW_DATE_FORMAT);
	}

	@Override
	public void process(QueryResult.Record srcRecord) throws OgaiException {
		//Форматируем только непустые значения
		if (srcRecord.containsKey(getName()) && (srcRecord.get(getName()) != null)) {
			String result = Util.formatDate((Date) srcRecord.get(getName()), formatString);
			srcRecord.put(getName(), result);
		}
	}
}
