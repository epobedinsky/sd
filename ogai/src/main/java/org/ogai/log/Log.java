package org.ogai.log;

import org.ogai.util.Util;

/**
 * Лог.
 *
 * Чтоб получить лог для своего класса вызовите Log log = new Log(YourClass.class) можно статически
 *
 * Чтоб начать использовать лог вызовите при старте приложения из потокобезопасного кода
 *
 * Log.initLogService(YourLogService serviceArg)
 * При завершении работы приложение вызовите Log.closeLogService() чтоб очистить все занятые логом ресурсы
 *
 * @author Побединский Евгений
 *         25.03.14 20:23
 */
public class Log {
	private String name;
	private static LogService service;

	/**
	 * Инициализировать лог сервисом логгирования.
	 * Должно быть вызвано один раз в течение жизни приложения из потокобезопасного кода
	 * 
	 * @param serviceArg Log Service
	 * @return service
	 */
	public static LogService initLogService(LogService serviceArg) {
		assert serviceArg != null : "service is null";
		assert Log.service == null : "should be called once during system lifecycle";
		
		Log.service = serviceArg;
		return Log.service;
	}

	/**
	 * Выполняет все необходимые операции для завершения работы лога и закрытия всех его ресурсов
	 */
	public static synchronized void closeLogService() {
		if (service != null) {
			service.close();
		}

		service = null;
	}
	
	public Log(String name) {
		assert Util.isNotEmpty(name) : "log name is empty";

		this.name = name;
	}

	public void debug(String s) {
		getService().debug(name, s);
	}

	public void error(String s, Throwable e) {
		getService().error(name, s, e);
	}

	public void error(String s) {
		getService().error(name, s);
	}

	public void error(Throwable e) {
		getService().error(name, "", e);
	}

	public void info(String s) {
		getService().info(name, s);
	}

	public void warn(String s, Throwable e) {
		getService().warn(name, s, e);
	}

	public void warn(String s) {
		getService().warn(name, s);
	}
	
	private LogService getService() {
		assert service != null : "service should be init by initLogService call";

		return service;
	}
}
