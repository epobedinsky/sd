package org.ogai.core;

import org.ogai.command.sys.*;
import org.ogai.db.StoredDBQueryService;
import org.ogai.db.types.DatabaseService;
import org.ogai.db.types.MySQLDatabaseService;
import org.ogai.db.types.PostgresDatabaseService;
import org.ogai.grid.GridCommand;
import org.ogai.log.Log;
import org.ogai.log.LogFactory;
import org.ogai.log.LogService;
import org.ogai.test.TestCommand;
import org.ogai.text.TextService;
import org.ogai.view.elements.LabelElement;
import org.ogai.view.elements.LinkElement;
import org.ogai.view.html.renderers.HTMLLabelRenderer;
import org.ogai.view.html.renderers.HTMLLinkRenderer;

import javax.servlet.ServletContext;

/**
 * Entire application object
 *
 * @author Побединский Евгений
 *         23.03.14 16:48
 */
public class Application {
	public static Log log = LogFactory.create("APP");

	private static final String LOGGER_NOT_READY_MESSAGE = "System log not ready";

	public static enum AppState {
		NOT_INITIALIZED,
		INITIALIZED,
		ERROR
	};

	public static AppState state = AppState.NOT_INITIALIZED;

	private static ServletContext servletContext;

	public static synchronized boolean init(ServletContext servletContext){
		Application.servletContext = servletContext;

		//Нельзя повторно запустить неправильно запущенное приложение
		if (state == AppState.ERROR) {
			return false;
		}

		//Создаем реестр сервисов
		ServicesRegistry registry = ServicesRegistry.createInstance();

		boolean result = false;
		try {
			//Готовим сервисы
			//и прежде всего лог и сервис логгирования
			registry.register(LogService.NAME, Log.initLogService(LogFactory.createSystemLogService()));
			//registry.register(LogService.NAME, Log.initLogService(LogFactory.createFileLogService()));
			//registry.register(DatabaseService.NAME, new PostgresDatabaseService());
			registry.register(DatabaseService.NAME, new MySQLDatabaseService());
			log.info("Application init started");

			registry.register(TextService.NAME, new TextService());
			registry.register(StoredDBQueryService.NAME,  new StoredDBQueryService());

			//create comands registry and add all system commands
			log.info("System commands registration started");
			CommandsRegistry commandsRegistry = CommandsRegistry.createInstance();
			commandsRegistry.register(InitCommand.NAME, new InitCommand());
			commandsRegistry.register(LoginCommand.NAME, new LoginCommand());
			commandsRegistry.register(LogoutCommand.NAME, new LogoutCommand());
			commandsRegistry.register(TestCommand.NAME, new TestCommand());
			commandsRegistry.register(GridCommand.NAME,  new GridCommand());
			commandsRegistry.register(GoCommand.NAME,  new GoCommand());
			commandsRegistry.register(DeleteCommand.NAME,  new DeleteCommand());
			commandsRegistry.register(SubmitSaveCommand.NAME, new SubmitSaveCommand());


			//Создаем реестр объектов
			ObjectsRegistry objectsRegistry = ObjectsRegistry.createInstance();

			//Рендереры в HTML
			objectsRegistry.register(LabelElement.NAME,  HTMLLabelRenderer.class);
			objectsRegistry.register(LinkElement.NAME, HTMLLinkRenderer.class);


			log.info("Application init finished");
			state = AppState.INITIALIZED;
			result = true;
		} catch (Throwable te) {
			logErrorSafe("Error on Application init", te);
			state = AppState.ERROR;
			Application.destroy();
		}
		return result;
	}

	public static synchronized void setError() {
		state = AppState.ERROR;
	}

	public static synchronized void destroy() {
		assert state != AppState.NOT_INITIALIZED : "Application was already destroyed";

		//Создаем системный контекст
		Ctx sysContext = new Ctx();

		try {
			logInfoSafe("Application destroy started");
			//Если приложение работало нормально, устанавливаем ему статус неиницилизировано
			//Если в состоянии ошибка, оставляем в этом
			if (state == AppState.INITIALIZED) {
				state = AppState.NOT_INITIALIZED;
			}

			//Закрываем все закрываемые сервисы кроме логирования
			ServicesRegistry.getInstance().closeAllExcept(LogService.NAME);

			//Записыаем последнюю строку
			logInfoSafe("Application closed with state: " + state + ". Good buy! :)");
			//Закрываем сервис логгирования, если мы успели его добавить до ошибки на старте приложения
			if (ServicesRegistry.getInstance().has(LogService.NAME)) {
				((Closeable)ServicesRegistry.getInstance().get(LogService.NAME)).close();
			}
		} catch (Throwable t) {
			//Любые ошибки при уничтожении приложения должны умалчиваться
			logErrorSafe("Error on application destroy", t);
		}
	}

	/**
	 * Пишет message в наш лог, если он готов, если нет - в System
	 * //TODO писать вместо System в логи томката
	 * @param message
	 * @param error если не null то будет записан StackTrace
	 */
	public static void logErrorSafe(String message, Throwable error) {
		if (ServicesRegistry.getInstance().has(LogService.NAME)) {
			if (error != null) {
				log.error(message, error);
			} else {
				log.error(message);
			}
		} else {
			//TODO научиться писать в лог томката
			if (error != null){
				System.err.println(LOGGER_NOT_READY_MESSAGE + "." + message + ": " + error.getMessage());
				error.printStackTrace(System.err);
			} else {
				System.err.println(LOGGER_NOT_READY_MESSAGE);
			}
		}
	}

	/**
	 * Пишет message в наш лог, если он готов, если нет - в System
	 * //TODO писать вместо System в логи томката
	 * @param message
	 */
	public static void logInfoSafe(String message) {
		if (ServicesRegistry.getInstance().has(LogService.NAME)) {
			log.info(message);
		} else {
			System.out.println(message);
		}
	}

	public static boolean isInitialized() {
		return state == AppState.INITIALIZED;
	}
}
