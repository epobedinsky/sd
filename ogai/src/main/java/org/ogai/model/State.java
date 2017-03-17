package org.ogai.model;

import org.ogai.db.QueryResult;
import org.ogai.util.Util;

import java.util.HashMap;

/**
 * Состояние stateless - обьекта модели.
 * Необходимо чтоб передавать состояние по разным этапам жизненного цикла объекта модели
 *
 * @author Побединский Евгений
 *         15.04.14 22:43
 */
public class State extends HashMap<String, Object> {
	private QueryResult data;
	private String id;
	private String name;

	public State(String id, String name) {
		assert Util.isNotEmpty(name);

		this.id = id;
		this.name = name;
	}

	public QueryResult getData() {
		return data;
	}

	public void setData(QueryResult data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isInsert() {
		return Util.isEmpty(id);
	}
}
