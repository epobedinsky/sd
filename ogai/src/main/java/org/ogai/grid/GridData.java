package org.ogai.grid;

import org.ogai.db.QueryResult;

/**
 * Абстракция данных для грида
 *
 * @author Побединский Евгений
 *         06.04.14 23:15
 */
public class GridData {
	private QueryResult queryResult;

	public GridData(QueryResult queryResult) {
		assert queryResult != null;

		this.queryResult = queryResult;
	}

	public QueryResult getQueryResult() {
		return queryResult;
	}
}
