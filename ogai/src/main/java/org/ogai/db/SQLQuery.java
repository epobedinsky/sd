package org.ogai.db;

import org.ogai.model.table.Table;
import org.ogai.util.Util;

import java.util.Date;
import java.util.Set;

/**
 * Абстракция SQL - запроса к БД
 *
 * @author Побединский Евгений
 *         06.04.14 23:22
 */
public class SQLQuery {
	//TODO костыль убрать после реализации системы преобразований
	public static final String COL_CREATE_DATE = "create_date";

	protected final String SELECT_SQL = "SELECT * FROM %s";
	protected final String INSERT_SQL = "INSERT INTO %s (%s) VALUES(%s)";
	protected final String UPDATE_SQL = "UPDATE %s SET %s";
	protected final String DELETE_SQL = "DELETE FROM %s";
	protected final String WHERE_CLAUSE = " WHERE";
	protected final String EQUALS = " %s = %s";

	protected final String AND = " AND";
	protected final String COMMA = " ,";

	private String queryText;

	public SQLQuery() {
	}

	public SQLQuery(String query) {
		assert Util.isNotEmpty(query);

		this.queryText = query;
	}

	public String getQuery() {
		return queryText;
	}

	public void setQuery(String query) {
		assert Util.isNotEmpty(query);

		this.queryText = query;
	}

	public SQLQuery insertQuery(Table table, QueryResult.Record record) {
		return createInstance(String.format(INSERT_SQL, table.getTableName(), //INSERT INTO tableName
				createColumnsList(record, table.getIdColumnName()), // (id, a1, b1...n)
				createValuesListForInsert(record, table.getIdColumnName())));
	}

	public SQLQuery updateQuery(Long id, Table table, QueryResult.Record record) {
		return createInstance(String.format(UPDATE_SQL, table.getTableName(), //UPDATE
				createEqOperatorsList(COMMA, record, table.getIdColumnName()) //список операторов присваивания a1 = a, b1 = b...
						//WHERE id = idValue
						+  WHERE_CLAUSE + createOperatorStatement(EQUALS, table.getIdColumnName(), id)));
	}

	public SQLQuery deleteQuery(Long id, Table table) {
		return createInstance(String.format(DELETE_SQL + WHERE_CLAUSE + EQUALS, table.getTableName(), table.getIdColumnName(), id.toString()));
	}

	public SQLQuery listQuery(Table table, QueryResult.Record filter) {
		return createInstance(String.format(SELECT_SQL + (filter.isEmpty() ? "" : WHERE_CLAUSE), table.getTableName())
				+ (filter.isEmpty() ? "" : createCondition(filter)));
	}

	public SQLQuery loadQuery(String id, Table table) {
		return createInstance(String.format(SELECT_SQL + WHERE_CLAUSE + EQUALS, table.getTableName(), table.getIdColumnName(), id));
	}

	/**
	 * Перегрузить в наследниках, чтоб возвращали объект актуального типа
	 * @param queryText
	 * @return
	 */
	public SQLQuery createInstance(String queryText) {
		return new SQLQuery(queryText);
	}

	protected  String createCondition(QueryResult.Record filter) {
		return createEqOperatorsList(AND, filter);
	}

	protected  String createEqOperatorsList(String delimiter, QueryResult.Record filter, String... excludeColumns) {
		assert Util.isNotEmpty(delimiter);

		Set<String> excludeColumnsSet = Util.toSet(excludeColumns);

		StringBuilder sb = new StringBuilder();
		boolean isFirstParam = true;
		for (String column : filter.keySet()) {
			if (!excludeColumnsSet.contains(column)) {
				String paramConditionFormat = EQUALS;
				if (!isFirstParam) {
					paramConditionFormat = delimiter + paramConditionFormat;
				} else {
					isFirstParam = false;
				}
				sb.append(createOperatorStatement(paramConditionFormat, column, filter.get(column)));
			}
		}

		return sb.toString();
	}

	protected  String createOperatorStatement(String operatorFormat, String column, Object value) {
		assert Util.isNotEmpty(operatorFormat);
		assert Util.isNotEmpty(column);

		return String.format(operatorFormat, column, getSQLParam(column, value));
	}

	protected  String createColumnsList(QueryResult.Record record, String idColumnName) {
		StringBuilder sb = new StringBuilder();
		boolean isFirstParam = true;
		for (String column : record.keySet()) {
			String value = column;
			if (!isFirstParam) {
				value = COMMA + value;
			} else {
				isFirstParam = false;
			}
			sb.append(value);
		}

		return sb.toString();
	}

	protected  String createValuesList(QueryResult.Record record, String... excludeColumns) {
		Set<String> excludeColumnsSet = Util.toSet(excludeColumns);

		StringBuilder sb = new StringBuilder();
		boolean isFirstParam = true;
		for (String column : record.keySet()) {
			if (!excludeColumnsSet.contains(column)) {
				String paramConditionFormat = getSQLParam(column, record.get(column));
				if (!isFirstParam) {
					paramConditionFormat = COMMA + paramConditionFormat;
				} else {
					isFirstParam = false;
				}
				sb.append(paramConditionFormat);
			}
		}

		return sb.toString();
	}

	protected  String createValuesListForInsert(QueryResult.Record record, String idColumnName) {
		StringBuilder sb = new StringBuilder();
		boolean isFirstParam = true;
		for (String column : record.keySet()) {
			String paramConditionFormat = !column.equals(idColumnName) ? getSQLParam(column, record.get(column))
					: "%s";

			if (!isFirstParam) {
				paramConditionFormat = COMMA + paramConditionFormat;
			} else {
				isFirstParam = false;
			}
			sb.append(paramConditionFormat);
		}

		return sb.toString();
	}

	/**
	 *
	 * @return Параметр оформленный для использования в запросе
	 */
	protected  String getSQLParam(String column, Object value) {
		String result = "";
		//TODO переделать на систему преобразователей
		if (column.equals(COL_CREATE_DATE)) {
			if (value == null) {
				result = "null";
			}
			return "'" + Util.formatDate((Date)value,Util.DB_DATETIME_FORMAT_PATTERN ) + "'";
		} else {
			if (value == null) {
				result = "''";
			}
				//никакого специфичного преобразования не найдено - считаем что это строковый параметр
				result =  "'" + String.valueOf(value) + "'";
			}

		return result;
	}
}
