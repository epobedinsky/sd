package org.ogai.db;

import org.ogai.exception.OgaiException;

/**
 * Исключение связанное со слоем БД
 *
 * @author Побединский Евгений
 *         30.03.14 18:56
 */
public class DBException extends OgaiException {
	public DBException() {
		super();   
	}

	public DBException(String message) {
		super(message);   
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);   
	}

	public DBException(Throwable cause) {
		super(cause);   
	}

	public DBException(String message, String userDisplayMessage) {
		super(message, userDisplayMessage);   
	}

	public DBException(String message, String userDisplayMessage, Throwable cause) {
		super(message, userDisplayMessage, cause);   
	}

	public DBException(Throwable cause, String userDisplayMessage) {
		super(cause, userDisplayMessage);   
	}
}
