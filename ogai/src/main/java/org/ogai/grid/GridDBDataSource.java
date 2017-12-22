package org.ogai.grid;

import org.ogai.core.ServicesRegistry;
import org.ogai.db.DBSession;
import org.ogai.db.SQLQuery;
import org.ogai.db.types.DatabaseService;
import org.ogai.exception.OgaiException;

/**
 * Источник данных их БД для грида
 *
 * @author Побединский Евгений
 *         06.04.14 23:20
 */
public class GridDBDataSource implements GridDataSource {
	private SQLQuery query;
	protected DatabaseService dbService;

	public GridDBDataSource(SQLQuery query) {
		assert query != null;

		this.query = query;
		dbService = (DatabaseService) ServicesRegistry.getInstance().get(DatabaseService.NAME);
	}

	@Override
	public GridData getData() throws OgaiException {
		//Пока просто делаем запрос
		//TODO реализовать сортировки пейджинг и филтрацию
		SQLQuery sqlQuery = dbService.getQuery();
		sqlQuery.setQuery(query.getQuery());
		return new GridData(DBSession.selectQuery(sqlQuery));
	}
}
