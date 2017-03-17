package org.ogai.grid;

import org.ogai.db.DBSession;
import org.ogai.db.SQLQuery;
import org.ogai.exception.OgaiException;

/**
 * Источник данных их БД для грида
 *
 * @author Побединский Евгений
 *         06.04.14 23:20
 */
public class GridDBDataSource implements GridDataSource {
	private SQLQuery query;

	public GridDBDataSource(SQLQuery query) {
		assert query != null;

		this.query = query;
	}

	@Override
	public GridData getData() throws OgaiException {
		//Пока просто делаем запрос
		//TODO реализовать сортировки пейджинг и филтрацию
		return new GridData(DBSession.selectQuery(query.getQuery()));
	}
}
