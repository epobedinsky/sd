package org.ogai.db;

import org.ogai.log.Log;
import org.ogai.log.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * Логгирует ошибкии варнинги после выполнения SQL - запросов
 * TODO переписать
 *
 *         30.03.14 19:18
 */
public class JDBCExceptionReporter {

	public static final Log log = LogFactory.create(JDBCExceptionReporter.class);
	public static final String DEFAULT_EXCEPTION_MSG = "SQL Exception";
	public static final String DEFAULT_WARNING_MSG = "SQL Warning";

	private JDBCExceptionReporter() {}

	public static void logAndClearWarnings(Statement statement){
		try {
			logWarnings( statement.getWarnings() );
		}
		catch (SQLException sqle) {
			log.error("could not log warnings", sqle);
		}
		try {
			statement.clearWarnings();
		}
		catch (SQLException sqle) {
			log.error("could not clear warnings", sqle);
		}
	}

	public static void logAndClearWarnings(Connection connection) {
		try {
			logWarnings( connection.getWarnings() );
		}
		catch (SQLException sqle) {
			log.error("could not log warnings", sqle);
		}
		try {
			connection.clearWarnings();
		}
		catch (SQLException sqle) {
			log.error("could not clear warnings", sqle);
		}
	}

	public static void logWarnings(SQLWarning warning) {
		logWarnings(warning, null);
	}

	public static void logWarnings(SQLWarning warning, String message) {
		if (warning != null ) {
			message = isNotEmpty(message) ? message : DEFAULT_WARNING_MSG;
			log.warn( message, warning );
		}
		while (warning != null) {
			StringBuffer buf = new StringBuffer(30)
					.append( "SQL Warning: ")
					.append( warning.getErrorCode() )
					.append( ", SQLState: ")
					.append( warning.getSQLState() );
			log.warn( buf.toString() );
			log.warn( warning.getMessage() );
			warning = warning.getNextWarning();
		}
	}

	public static void logExceptions(SQLException ex) {
		logExceptions(ex, null);
	}

	public static void logExceptions(SQLException ex, String message) {
		message = isNotEmpty(message) ? message : DEFAULT_EXCEPTION_MSG;
		log.error( message, ex );
		while (ex != null) {
			StringBuffer buf = new StringBuffer(30)
					.append( "SQL Error: " )
					.append( ex.getErrorCode() )
					.append( ", SQLState: " )
					.append( ex.getSQLState() );
			log.error( buf.toString() );
			log.error( ex.getMessage() );
			ex = ex.getNextException();
		}
	}

	private static boolean isNotEmpty(String string) {
		return string != null && string.length() > 0;
	}

}