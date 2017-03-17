package org.ogai.log;

/**
 * Интерфейс сервиса логгирования
 *
 * @author Побединский Евгений
 *         25.03.14 20:39
 */
public interface LogService {
	public static final String NAME = "log";

	public static enum LogLevel {
		ERROR,
		INFO,
		WARN,
		DEBUG;

		public boolean isChecked(LogLevel type) {
			return this.ordinal() >= type.ordinal();
		}
	}

	/**
	 * Выполняет все необходимые операции для завершения работы лога и закрытия всех его ресурсов
	 */
	void close();

	void debug(String name, String s);

	void error(String name, String s);

	void error(String name, String s, Throwable e);

	void info(String name, String s);

	void warn(String name, String s, Throwable e);

	void warn(String name, String s);

}
