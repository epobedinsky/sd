package org.ogai.log.log4j;

import org.apache.logging.log4j.LogManager;
import org.ogai.log.LogService;

/**
 * Реализация логгирования с помощью библиотеки Log4j
 *
 * @author Побединский Евгений
 *         25.03.14 21:11
 */
public class Log4jService implements LogService {
	@Override
	public void close() {
	}

	@Override
	public void debug(String name, String s) {
		getLogger(name).debug(name, s);
	}

	@Override
	public void error(String name, String s) {
		getLogger(name).error(name, s);
	}

	@Override
	public void error(String name, String s, Throwable e) {
		getLogger(name).error(name, s, e);
	}

	@Override
	public void info(String name, String s) {
		getLogger(name).info(name, s);
	}

	@Override
	public void warn(String name, String s, Throwable e) {
		getLogger(name).warn(name, s, e);
	}

	@Override
	public void warn(String name, String s) {
		getLogger(name).warn(name, s);
	}

	private  org.apache.logging.log4j.Logger getLogger(String name) {
		return LogManager.getLogger(name);
	}
}
