package org.ogai.db;

import org.ogai.core.Application;
import org.ogai.core.Ctx;
import org.ogai.core.ServicesRegistry;
import org.ogai.db.types.DatabaseService;
import org.ogai.exception.OgaiException;
import org.ogai.log.Log;
import org.ogai.log.LogFactory;
import org.ogai.model.table.Table;
import org.ogai.util.Util;
import org.ogai.db.QueryResult.Record;

import java.sql.*;

/**
 * Сессия работы с БД
 *
 * @author Побединский Евгений
 *         30.03.14 21:44
 */
public class DBSession {
	private static final Log log = LogFactory.create(DBSession.class);

	private Ctx ctx;
	private ConnectionManager connectionManager;
	private Transaction transaction;

	//Сколько раз была открыта сессия за время ее жизни
	private int nestedSessionsCounter;

	/**
	 *
	 * @return Открытая сессия если есть или новая, если еще нет
	 */
	public static DBSession get() {
		DBSession current = Ctx.get().getCurrentDBSession();
		if (current == null) {
			current = new DBSession();
			log.info("Created DB session:"+ current.hashCode());
		} else {
			log.debug("DBSession is already opened:" + current.hashCode());
		}
		return current;
	}

	/**
	 * Открыть новую сессию
	 */
	public void open() {
		//Наращиваем счетчик открытий
		nestedSessionsCounter++;

		//Открываем JDBC транзакцию
		if (transaction.begin()) { //exception
			log.info("Opened session " + this.hashCode() + ":" + nestedSessionsCounter);
		}

		//Устанавливаем в текущем контексте эту сессию как текущую
		ctx.setCurrentDBSession(this);
	}

	/**
	 * Пометить сессию для отката
	 */
	public void markForRollback() {
		transaction.setRollbackOnly();
	}

	/**
	 * Закрыть сессию (если перед этим она была помечена для отката вызовом markForRollback
	 * все изменения будут отменены)
	 */
	public void close() throws DBException {
		close(false);
	}

	/**
	 * Может использоваться как внутри транзакции так и вне
	 *
	 * @param sql Непустая строка
	 * @return Результаты выполнения запроса или пустой объект QueryResult если ничего не вернулось
	 * @throws OgaiException
	 */
	public QueryResult select(String sql) throws OgaiException {
		assert Util.isNotEmpty(sql) : "Query is empty in select method";

		Statement st = null;
		ResultSet rs = null;
		try {
			//Открыли транзакцию для чтения
			open();
			log.info(sql);

			st = connectionManager.getConnection().createStatement();
			rs = st.executeQuery(sql);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			QueryResult gr = new QueryResult();


			while(rs.next()) {
				Record record = new Record();
				for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
					String columnName = rsMetaData.getColumnName(i + 1);
					Object value = rs.getObject(i + 1);
					record.put(columnName, value);
				}
				gr.add(record);
			}
			log.info("records count=" + gr.size());
			return gr;
		} catch (SQLException e) {
			//Помечаем для отката
			markForRollback();
			throw new DBException(e);
		} catch (Throwable t) {
			//Помечаем для отката
			markForRollback();
			throw new OgaiException("Error in select", t);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}

				if (st != null) {
					JDBCExceptionReporter.logAndClearWarnings(st);
					st.close();
				}
			} catch (SQLException e) {
				//Не получилось закрыть объекты просто отписываем в лог
				log.error("Error closing resultset or statement:" ,e);
			}

			//Закрыли транзакцию для чтения
			close();
		}
	}

	/**
	 *
	 * @param query
	 * @return результаты select - запроса содержащие набор строк, выполненного без объекта сессии
	 * @throws OgaiException
	 */
	public static QueryResult selectQuery(SQLQuery query) throws OgaiException {
		DBSession session = DBSession.get();
		return session.select(query.getQuery());
	}

	/**
	 *
	 * @param sql
	 * @return результат select - запроса содержащий одиночное значение, выполненного без объекта сессии
	 * @throws OgaiException
	 */
	public static Object getValueQuery(String sql) throws OgaiException {
		DBSession session = DBSession.get();
		return session.getValue(sql);
	}

	/**
	 *
	 * @param query Запрос связанный с выполнением изменений в таблице
	 * @return
	 * @throws OgaiException
	 */
	public static boolean executeQuery(SQLQuery query) throws OgaiException {
		DBSession session = DBSession.get();
		return session.execute(query.getQuery());
	}

	public Object getValue(String sql) throws OgaiException {
		QueryResult result = select(sql);

		if (result.size() > 1) {
			throw new OgaiException("Result set contains more then one record for getValue call");
		}

		if (result.get(0).values().size() > 1) {
			throw new OgaiException("Result set record contain more then one column for getValue call");
		}

		return result.get(0).values().toArray(new Object[0])[0];
	}

//	/**
//	 *
//	 * @param sequence
//	 * @return Текущее значение последовательности. Может быть вызвано только в сесии
//	 * (более того в транзакции, где был вызван nextval)
//	 * @throws OgaiException
//	 */
//	public Object getSequenceCurrentValue(String sequence) throws OgaiException {
//		return getValue("SELECT currval('" + sequence + "')");
//	}

	/**
	 * TODO переписать чтоб на вход подавался обьектный набор параметров
	 * @param sql
	 * @return
	 * @throws OgaiException
	 */
	public boolean execute(String sql) throws OgaiException {
		assert Util.isNotEmpty(sql) : "Query is empty in select method";

		Statement st = null;
		try {
			//Открыли транзакцию
			open();
			log.info(sql);

			st = connectionManager.getConnection().createStatement();
			boolean result = st.execute(sql);
			if (result) {
				log.info("execute sucessful");
			}
			return result;
		} catch (SQLException e) {
			//Помечаем для отката
			markForRollback();
			throw new DBException(e);
		} catch (Throwable t) {
			//Помечаем для отката
			markForRollback();
			throw new OgaiException("Error in execute", t);
		} finally {

			try {
				if (st != null) {
					JDBCExceptionReporter.logAndClearWarnings(st);
					st.close();
				}
			} catch (SQLException e) {
				//Не получилось закрыть объекты просто отписываем в лог
				log.error("Error closing statement:" ,e);
			}

			//Закрыли транзакцию для чтения
			close();
		}
	}

	/**
	 * Выполняет вставку в таблицу где id генерируется с помощью sequence
	 * @param table не Null
	 * @param query Строка sql из которой будет получен финальный скрипт вставки. В нее будут подставлено
	 *               вычисление его значения из sequence
	 * @return id добавленной записи
	 */
	public static Long executeInsertQuery(Table table, SQLQuery query) throws OgaiException {
		assert table != null;
		assert query != null;

		DBSession dbSession = DBSession.get();

		//Тест 1 Инсерт в транзакции c откатом
		try {
			dbSession.open();

			//Готовим insert sql
			DatabaseService dbService = (DatabaseService) ServicesRegistry.getInstance().get(DatabaseService.NAME);
			String sql = insertIdGeneration(query, dbService.getIdGenerator().getGenerationIdSentence(table));
			//Вставляем
			dbSession.execute(sql);
			return (Long)dbService.getIdGenerator().getCurrentId(table, dbSession);

		} catch(Exception e) {
			dbSession.markForRollback();
			throw new OgaiException("Error on insert " + e.getMessage(), e);
		} finally {
			dbSession.close();
		}
	}

	private static String insertIdGeneration(SQLQuery query, String idGeneration) {
		if (Util.isEmpty(idGeneration)) {
			return query.getQuery();
		}
		return String.format(query.getQuery(), idGeneration);
	}

	public Long executeInsertQueryInTrx(Table table, SQLQuery query) throws OgaiException {
		assert table != null;
		assert query != null;

		//Готовим insert sql
		DatabaseService dbService = (DatabaseService) ServicesRegistry.getInstance().get(DatabaseService.NAME);
		String sql = insertIdGeneration(query, dbService.getIdGenerator().getGenerationIdSentence(table));
		//Вставляем
		this.execute(sql);
		return (Long)dbService.getIdGenerator().getCurrentId(table, this);
	}

//	private static Object getSequenceNextValue(String sequenceName) {
//		assert Util.isNotEmpty(sequenceName);
//
//		return "nextval('" + sequenceName + "')";
//	}


	@Override
	protected void finalize() throws Throwable {
		try {
			close(true); //eception
		} catch (Throwable te) {
			Application.logErrorSafe("Error finalyzing session", te);
		}
		super.finalize();
	}

	private DBSession() {
		ctx = Ctx.get();
		connectionManager = new ConnectionManager();
		transaction = new JDBCTransaction(connectionManager);
		nestedSessionsCounter = 0;
	}

	private void close(boolean isFinal) throws DBException {
		log.info("Closing session:" + this.hashCode());
		//если это вложенная сессия ничего не делаем, отматываем счетчик
		if (nestedSessionsCounter > 1) {
			//откатываем счетчик
			nestedSessionsCounter--;
			//только если мы не финилизируем объект сессии
			if (!isFinal) {
				return;
			}
		}

		//если connectionManager еще не закрыт
		if (!connectionManager.isClosed()) {
			try {
				//коммитим
				transaction.commit(); //exception
			} finally {
				nestedSessionsCounter = 0;
				ctx.clearCurrentDBSession();
				connectionManager.closeConnection(); //exception
				log.info("Session:" + this.hashCode() + " closed!");
			}
		}
	}
}
