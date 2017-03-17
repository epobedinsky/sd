package org.ogai.db;

/**
 * Implements a basic transaction strategy for JDBC connections.This is the
 * default <tt>Transaction</tt> implementation used if none is explicitly
 * specified.
 * @author Anton van Straaten, Gavin King
 */
import org.ogai.exception.OgaiException;
import org.ogai.log.Log;
import org.ogai.log.LogFactory;

import java.sql.SQLException;


/**
 * Implements a basic transaction strategy for JDBC connections.This is the
 * default <tt>Transaction</tt> implementation used if none is explicitly
 * specified.
 * @author Anton van Straaten, Gavin King
 */
public class JDBCTransaction implements Transaction {

	private static final Log log = LogFactory.create(JDBCTransaction.class);

	private ConnectionManager connectionManager;

	private boolean toggleAutoCommit;
	private TransactionStatus status;
	private int timeout = -1;

	public JDBCTransaction(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
		this.status = TransactionStatus.NO_TRANSACTION;
	}

	public boolean begin() throws TransactionException {
		if (status != TransactionStatus.NO_TRANSACTION) return false;

		try {
			toggleAutoCommit = connectionManager.getConnection().getAutoCommit();

			if (toggleAutoCommit) {
				connectionManager.getConnection().setAutoCommit(false);
			}
		}
		catch (SQLException e) {
			log.error("JDBC begin failed", e);
			throw new TransactionException("JDBC begin failed: ", e);
		}
		catch (OgaiException e){
			log.error(e);
			throw new TransactionException("JDBC begin failed: ", e);
		}

		status = TransactionStatus.ACTIVE;

		return true;
	}

	public void commit() throws TransactionException {
		if (status == TransactionStatus.MARKED_ROLLBACK) {
			rollback();
		}
		else if (status != TransactionStatus.ACTIVE){
			throw new TransactionException("Transaction not successfully started. TransactionStatus " + status);
		} else {
			status = TransactionStatus.COMMITTING;
			try {
				commitAndResetAutoCommit();
				status = TransactionStatus.COMMITTED;
			}
			catch (OgaiException e) {
				log.error("JDBC commit failed", e);
				status = TransactionStatus.UNKNOWN;
				throw new TransactionException("JDBC commit failed", e);
			}
		}
	}

	private void commitAndResetAutoCommit() throws OgaiException {
		try {
			connectionManager.getConnection().commit();
		}
		catch (SQLException e){
			throw new OgaiException(e);
		}
		finally {
			toggleAutoCommit();
		}
	}

	public void rollback() throws TransactionException {
		log.debug("rollback");

		try {
			rollbackAndResetAutoCommit();
			status = TransactionStatus.ROLLEDBACK;
		}
		catch (OgaiException e) {
			log.error("JDBC rollback failed", e);
			status = TransactionStatus.UNKNOWN;
			throw new TransactionException("JDBC rollback failed", e);
		}
	}

	private void rollbackAndResetAutoCommit() throws OgaiException {
		try {
			connectionManager.getConnection().rollback();
		}
		catch (SQLException e){
			throw new OgaiException(e);
		}
		finally {
			toggleAutoCommit();
		}
	}

	private void toggleAutoCommit() {
		try {
			if (toggleAutoCommit) {
				connectionManager.getConnection().setAutoCommit( true );
			}
		}
		catch (Exception sqle) {
			log.error("Could not toggle autocommit", sqle);
		}
	}

	public int getStatus(){
		return status.getTransactionStatusCode();
	}

	public void setRollbackOnly(){
		status = TransactionStatus.MARKED_ROLLBACK;
	}

	public void setTimeout(int seconds) {
		timeout = seconds;
	}

	public int getTimeout() {
		return timeout;
	}
}
