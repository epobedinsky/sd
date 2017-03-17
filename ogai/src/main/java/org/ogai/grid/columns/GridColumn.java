package org.ogai.grid.columns;

import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.util.Util;

/**
 * Простой столбец грида. Содержит имя и не выполняет никаких преобразований исходного значения
 *
 * @author Побединский Евгений
 *         10.04.14 21:02
 */
public class GridColumn {
	private String name;

	/**
	 *
	 * @param name Не пустое имя столбца. Именно с таким именем значение из результатов запроса пойдет в грид
	 */
	public GridColumn(String name) {
		assert Util.isNotEmpty(name);

		this.name = name;
	}

	/**
	 * Преобразовать значение из QueryResult для отображения в гриде
	 * @param srcRecord запись
	 * 		   Эта реализация не делает ничего с исходным значением.
	 * 		   Если что-то нужно будет делать для какого-то определенного типа столбца унаследоваться
	 */
	public void process(QueryResult.Record srcRecord) throws OgaiException {
		//
	}

	public String getName() {
		return name;
	}
}
