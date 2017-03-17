package org.ogai.db;

import java.util.ArrayList;
import java.util.HashMap;
import org.ogai.db.QueryResult.Record;

/**
 * Результаты выполнения select - запроса (список пара ключ-значение)
 *
 * @author Побединский Евгений
 *         01.04.14 20:38
 */
public class QueryResult extends ArrayList<Record> {
	public static class Record extends HashMap<String, Object> {

	}
}
