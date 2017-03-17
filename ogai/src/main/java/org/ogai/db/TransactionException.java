package org.ogai.db;


/**
 * Indicates that a transaction could not be begun, committed
 * or rolled back.
 *
 * @author Anton van Straaten
 * @see Transaction
 */

public class TransactionException extends RuntimeException {

	static final long serialVersionUID = 1L;

	public TransactionException(String message, Exception root) {
		super(message, root);
	}

	public TransactionException(String message) {
		super(message);
	}

}
