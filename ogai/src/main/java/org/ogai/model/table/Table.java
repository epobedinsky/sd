package org.ogai.model.table;

import org.ogai.core.ObjectsRegistry;
import org.ogai.db.DBSession;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.util.Util;

import java.util.HashSet;
import java.util.Set;

/**
 * Обертка над таблицей базы данных Конструирует SQL запросы для работы с некоторой сущности.
 * Идиентифицируется именем таблицы. Такое же имя должно быть у обьекта Entity.
 * Создаваться должен в потокобезопасном коде единожды для каждого tableName
 *
 * Stateless потому что для обработки всех экземпляров записей в таблице используется один объект
 * Ссылки на него содержатся в Entity и в ObjectsRegistry
 *
 * В наследниках нужно перегрузить метод define() чтоб определить все входящие в эту таблицу столбцы
 *
 * @author Побединский Евгений
 *         15.04.14 18:05
 */
public abstract class Table {
	protected static final String ID_COLUMN_NAME = "id";

	protected static final String SELECT_SQL = "SELECT * FROM %s";
	protected static final String INSERT_SQL = "INSERT INTO %s (%s) VALUES(%s)";
	protected static final String UPDATE_SQL = "UPDATE %s SET %s";
	protected static final String DELETE_SQL = "DELETE FROM %s";
	protected static final String WHERE_CLAUSE = " WHERE";
	protected static final String EQUALS = " %s = %s";

	protected static final String AND = " AND";
	protected static final String COMMA = " ,";

	protected final String tableName;


	//был ли вызван метод define();
	protected boolean isDefined;
	//имя поля с id
	protected String idColumnName;
	//Имя сиквенса для id
	protected String sequenceName;

	/**
	 *
	 * @param tableName не пустое имя таблицы
	 */
	public Table(String tableName, String sequenceName) {
		assert Util.isNotEmpty(tableName);

		this.tableName = tableName;
		this.isDefined = false;
		this.sequenceName = sequenceName;

		//имя по умолчанию
		idColumnName = ID_COLUMN_NAME;
	}

	public String getIdColumnName() {
		return idColumnName;
	}

	/**
	 * Зарегистрировать таблицу
	 * Вызывать из потокобезопасного кода один раз для каждого tableName
	 *
	 * Имя сущности. Связывает эту сущность со всеми другими связанными с ней объектами
	 */
	public void register() {
		//Настроить таблицу
		callDefine();
		ObjectsRegistry.getInstance().register(this);
	}

	/**
	 *
	 * @return Имя таблицы
	 */
	public String getTableName() {
		return tableName;
	}

	//////////////////////////
	/// Операции

	/**
	 * Просто вычитывает все столбцы по id. Для более сложной вычитки перегурзить этот метод в наследниках
	 * @param id
	 *
	 * @return QueryResult
	 * @throws
	 */
	public QueryResult load(String id) throws OgaiException {
		assert Util.isNotEmpty(id);
		assert isDefined;

		return DBSession.selectQuery(String.format(SELECT_SQL + WHERE_CLAUSE + EQUALS, tableName, idColumnName, id.toString())); //exception
	}

	/**
	 * Читает все столбцы во всх записях по заданному набору полей и их значений
	 * @param filter будет преобразовано в конструкцию вида WHERE a = a1 AND b = b1 ... AND N = n1
	 *
	 * @throws OgaiException
	 */
	public QueryResult list(QueryResult.Record filter) throws OgaiException {
		assert filter != null;

		return DBSession.selectQuery(String.format(SELECT_SQL + (filter.isEmpty() ? "" : WHERE_CLAUSE), tableName)
				+ (filter.isEmpty() ? "" : createCondition(filter))); //exception
	}

	/**
	 * Внести изменения в запись в таблице
	 * @param id записи Не пустое
	 * @param record
	 *
	 * @throws OgaiException
	 */
	public void update(Long id, QueryResult.Record record) throws OgaiException {
		assert record != null;
		assert id != null;

		DBSession.executeQuery(String.format(UPDATE_SQL, tableName, //UPDATE
				createEqOperatorsList(COMMA, record, idColumnName) //список операторов присваивания a1 = a, b1 = b...
				//WHERE id = idValue
				+  WHERE_CLAUSE + createOperatorStatement(EQUALS, idColumnName, id))); //exception
	}

	/**
	 * Вставить новую запись в таблицу
	 * @param record не null
	 *
	 * @throws OgaiException
	 */
	public Long insert(QueryResult.Record record) throws OgaiException {
		assert record != null;
		assert Util.isNotEmpty(this.sequenceName);

		return DBSession.executeInsertQuery(this.sequenceName, String.format(INSERT_SQL, tableName, //INSERT INTO tableName
				createColumnsList(record), // (id, a1, b1...n)
				createValuesListForInsert(record, idColumnName))); //VALUES(%s, a, b,...n) //exception
	}

	/**
	 * Вставить новую запись в таблицу
	 * @param record не null
	 *
	 * @throws OgaiException
	 */
	public Long insert(QueryResult.Record record, DBSession dbSession) throws OgaiException {
		assert record != null;
		assert Util.isNotEmpty(this.sequenceName);

		return dbSession.executeInsertQueryInTrx(this.sequenceName, String.format(INSERT_SQL, tableName, //INSERT INTO tableName
				createColumnsList(record), // (id, a1, b1...n)
				createValuesListForInsert(record, idColumnName))); //VALUES(%s, a, b,...n) //exception
	}

	/**
	 * Удалить из БД сущность по id
	 * @param id
	 * @throws OgaiException
	 */
	public void delete(Long id) throws OgaiException {
		assert id != null;
		assert isDefined;

		DBSession.executeQuery(String.format(DELETE_SQL + WHERE_CLAUSE + EQUALS, tableName, idColumnName, id.toString())); //exception
	}

	///////////////////////////

	/**
	 * Определить настройки таблицы (в основном столбцы)
	 */
	protected abstract void define();

	/**
	 *
	 * @return Параметр оформленный для использования в запросе
	 */
	protected String getSQLParam(String column, Object value) {
		//TODO найти соответствующий преобразователь если есть и пореобразовать
		//если нет
		if (value == null) {
			return "''";
		}

		//никакого специфичного преобразования не найдено - считаем что это строковый параметр
		return "'" + String.valueOf(value) + "'";
	}

	private void callDefine() {
		//таблица должна настраиваться строго один раз за время своей жизни
		assert !isDefined;

		define();
		isDefined = true;
	}

	private String createCondition(QueryResult.Record filter) {
		return createEqOperatorsList(AND, filter);
	}

	private String createEqOperatorsList(String delimiter, QueryResult.Record filter, String... excludeColumns) {
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

	private String createOperatorStatement(String operatorFormat, String column, Object value) {
		assert Util.isNotEmpty(operatorFormat);
		assert Util.isNotEmpty(column);

		return String.format(operatorFormat, column, getSQLParam(column, value));
	}

	protected String createColumnsList(QueryResult.Record record) {
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

	private String createValuesList(QueryResult.Record record, String... excludeColumns) {
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

	protected String createValuesListForInsert(QueryResult.Record record, String idColumnName) {
		StringBuilder sb = new StringBuilder();
		boolean isFirstParam = true;
		for (String column : record.keySet()) {
			String paramConditionFormat = !column.equals(idColumnName) ? getSQLParam(column, record.get(column))
					: " %s";

			if (!isFirstParam) {
				paramConditionFormat = COMMA + paramConditionFormat;
			} else {
				isFirstParam = false;
			}
			sb.append(paramConditionFormat);
		}

		return sb.toString();
	}
}
