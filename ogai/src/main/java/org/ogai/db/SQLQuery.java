package org.ogai.db;

import org.ogai.util.Util;

/**
 * Абстракция SQL - запроса к БД
 *
 * @author Побединский Евгений
 *         06.04.14 23:22
 */
public class SQLQuery {
	private String query;

	public SQLQuery(String query) {
		assert Util.isNotEmpty(query);

		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		assert Util.isNotEmpty(query);

		this.query = query;
	}
}
