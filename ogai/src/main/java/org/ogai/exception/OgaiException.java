package org.ogai.exception;

/**
 * Ошибка возникшая в библиотеке Ogai
 *
 * @author Побединский Евгений
 *         29.03.14 10:34
 */
public class OgaiException extends Exception {
	private String userDisplayMessage;

	public OgaiException() {
		super();    
	}

	public OgaiException(String message) {
		super(message);    
	}

	public OgaiException(String message, Throwable cause) {
		super(message, cause);    
	}

	public OgaiException(Throwable cause) {
		super(cause);    
	}

	public OgaiException(String message, String userDisplayMessage) {
		super(message);    
		this.userDisplayMessage = userDisplayMessage;
	}

	public OgaiException(String message, String userDisplayMessage, Throwable cause) {
		super(message, cause);    
		this.userDisplayMessage = userDisplayMessage;
	}

	public OgaiException(Throwable cause, String userDisplayMessage) {
		super(cause);    
		this.userDisplayMessage = userDisplayMessage;
	}

	public String getUserDisplayMessage() {
		return userDisplayMessage;
	}
}
