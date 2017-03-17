package org.ogai.db;

/**
 * Allows the application to define units of work, while
 * maintaining abstraction from the underlying transaction
 * implementation (eg. JTA, JDBC).<br>
 * <br>
 * A transaction is associated with a <tt>Session</tt> and is
 * usually instantiated by a call to <tt>Session.beginTransaction()</tt>.
 * A single session might span multiple transactions since
 * the notion of a session (a conversation between the application
 * and the datastore) is of coarser granularity than the notion of
 * a transaction. However, it is intended that there be at most one
 * uncommitted <tt>Transaction</tt> associated with a particular
 * <tt>Session</tt> at any time.<br>
 * <br>
 * Implementors are not intended to be threadsafe.
 *
 * @see Session#beginTransaction()
 * @see org.hibernate.transaction.TransactionFactory
 * @author Anton van Straaten
 */
public interface Transaction {

	/**
	 * Begin a new transaction.
	 */
	public boolean begin() throws TransactionException;

	/**
	 * Flush the associated <tt>Session</tt> and end the unit of work (unless
	 * we are in {@link FlushMode#NEVER}.
	 * </p>
	 * This method will commit the underlying transaction if and only
	 * if the underlying transaction was initiated by this object.
	 *
	 * @throws TransactionException
	 */
	public void commit() throws TransactionException;

	/**
	 * Force the underlying transaction to roll back.
	 *
	 * @throws TransactionException
	 */
	public void rollback() throws TransactionException;

	public int getStatus();

	/**
	 * Register a user synchronization callback for this transaction.
	 *
	 * @param synchronization The Synchronization callback to register.
	 * @throws HibernateException
	 */
	//public void registerSynchronization(Synchronization synchronization)
	//throws TransactionException;

	/**
	 * Set the transaction timeout for any transaction started by
	 * a subsequent call to <tt>begin()</tt> on this instance.
	 *
	 * @param seconds The number of seconds before a timeout.
	 */
	public void setTimeout(int seconds);

	public int getTimeout();

	public void setRollbackOnly();
}
