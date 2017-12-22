package org.ogai.log;

import org.ogai.log.log4j.Log4jService;
import org.ogai.log.mira.FileWriter;
import org.ogai.log.mira.SimpleLogFormatter;
import org.ogai.log.mira.SimpleLogService;
import org.ogai.log.mira.file.File;
import org.ogai.log.mira.file.Win7FileSystem;

/**
 * Фабрика логов
 *
 * @author Побединский Евгений
 *         25.03.14 21:40
 */
public class LogFactory {
	public static Log create(String name) {
		return new Log(name);
	}

	public static Log create(Class clazz) {
		assert clazz != null : "clazz is null";

		return new Log(clazz.getSimpleName());
	}

	//TODO переписать чтоб все это бралось из application.properties
	/**
	 * Создать логгер для записи в консоль
	 * @return
	 * @throws Exception
	 */
	public static LogService createSystemLogService() throws Exception {
		return new SimpleLogService(new SimpleLogFormatter());
	}

	/**
	 * Создать логгер для записи в файлы
	 * @return
	 * @throws Exception
	 */
	public static LogService createFileLogService() throws Exception {
		File logsDir = new File(new Win7FileSystem(), "../logs/applogs/");
		//File logsDir = new File(new Win7FileSystem(), "/usr/aqautor/logs");
		return new SimpleLogService(new SimpleLogFormatter(),
				new FileWriter("out.log", logsDir, 2000000, 100, "UTF-8"),
				new FileWriter("err.log", logsDir, 2000000, 100, "UTF-8"));
	}

	/**
	 * Логгер log4j
	 * @return
	 */
	public static LogService createLog4jLogService() {
		return new Log4jService();
	}
}
