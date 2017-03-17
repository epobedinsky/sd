package org.ogai.db;

/**
 * Статусы транзакций
 *
 * @author Побединский Евгений
 *         30.03.14 19:27
 */
public enum TransactionStatus {
	ACTIVE(0),
	MARKED_ROLLBACK(1),
	COMMITTED(3),
	ROLLEDBACK(4),
	UNKNOWN(5),
	NO_TRANSACTION(6),
	COMMITTING(8);

	private int transactionStatusCode;

	private TransactionStatus(int code) {
		transactionStatusCode = code;
	}

	public int getTransactionStatusCode() {
		return transactionStatusCode;
	}
}
