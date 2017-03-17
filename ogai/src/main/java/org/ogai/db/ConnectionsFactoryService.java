package org.ogai.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Интерфейс фабрики соединений с БД
 *
 * @author Побединский Евгений
 *         30.03.14 17:27
 */
public interface ConnectionsFactoryService {
	public static final String  NAME = "connectionsFactory";
	/**
	 *
	 * @return Соединение с БД
	 */
	public Connection create() throws DBException;

}
