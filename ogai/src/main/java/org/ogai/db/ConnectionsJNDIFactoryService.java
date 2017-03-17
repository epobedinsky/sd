package org.ogai.db;

import org.ogai.core.Closeable;
import org.ogai.exception.OgaiException;
import org.ogai.log.Log;
import org.ogai.log.LogFactory;
import org.ogai.util.Util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Фабрика соединений через JNDI
 * В идеале в системе должен жить один такой объект, который создается при иницилизации приложения
 * и удаляетсяпри его закрытии, т.е. это по сути сервис, так что он должен жить в ServicesRegistry
 *
 * @author Побединский Евгений
 *         30.03.14 17:31
 */
public class ConnectionsJNDIFactoryService implements ConnectionsFactoryService {
	private static Log log = LogFactory.create(ConnectionsJNDIFactoryService.class);

	private DataSource dataSource;

	public ConnectionsJNDIFactoryService(String jndiName) throws OgaiException {
		assert Util.isNotEmpty(jndiName) : "jndi is empty";

		try {
			log.info("Trying to create data source by jndiName:" + jndiName);
			Context jndiContext = new InitialContext();
			dataSource = (DataSource)jndiContext.lookup(jndiName);
			log.info("Create data source:" + dataSource + "by jndiName:" + jndiName);
		} catch (NamingException e) {
			throw new OgaiException("Can't get initial JNDI context", e);
		}

		if (dataSource == null) {
			throw new OgaiException("Failed to get datasource from JNDI by name:" + jndiName);
		}

	}

	@Override
	/**
	 * Все полученные этим методом соединения должны закрываться вызовом close
	 */
	public synchronized Connection create() throws DBException {
		assert null != dataSource;

		Connection connection =  null;
		try {
			connection =  dataSource.getConnection();
		} catch (SQLException e) {
			throw new DBException("Can't create connection", e);
		}

		return connection;
	}
}
