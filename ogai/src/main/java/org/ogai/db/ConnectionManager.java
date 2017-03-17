package org.ogai.db;

import org.ogai.core.ServicesRegistry;
import org.ogai.exception.OgaiException;
import org.ogai.log.Log;
import org.ogai.log.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Класс управляющий жизнью и состояние соединения с БД
 * новый поток получается вызовом сервиса какой-либо имплементации
 * @see org.ogai.db.ConnectionsFactoryService
 *
 * @author Побединский Евгений
 *         30.03.14 18:41
 */
public class ConnectionManager {
	private static Log log = LogFactory.create(ConnectionManager.class);

	private boolean isClosed;
	private Connection connection;
	private ConnectionsFactoryService connectionsFactory;

	public ConnectionManager() {
		assert ServicesRegistry.getInstance().has(ConnectionsFactoryService.NAME)
				: "connections factory service is not created yet";

		connectionsFactory = (ConnectionsFactoryService)ServicesRegistry.getInstance().get(ConnectionsFactoryService.NAME);
		isClosed = false;
	}

	/**
	 *
	 * @return Соединение с БД если оно уже было установлено или новое, если еще нет
	 * @throws OgaiException
	 */
	public Connection getConnection() throws OgaiException {
		if (isClosed) {
			throw new OgaiException("Connection manager is closed");
		}
		if (connection == null) {
			connection = createNewConnection();
			log.debug("Created connection:" + connection);
		}
		return connection;
	}

	/**
	 * Закрыть ConnectionManager
	 * @throws DBException
	 */
	public void closeConnection() throws DBException {
		assert connection != null : "Connection is null";

		try {
			log.debug("Closing connection:" + connection);
			//Записываем все замечания за время жизни соединения
			JDBCExceptionReporter.logAndClearWarnings(connection);
			connection.close();

		} catch (SQLException e) {
			throw new DBException("Error closing connection:" + connection);
		} finally {
			connection = null;
			isClosed = true;
		}
	}

	private Connection createNewConnection() throws DBException {
		return connectionsFactory.create();
	}

	public boolean isClosed() {
		return isClosed;
	}
}
