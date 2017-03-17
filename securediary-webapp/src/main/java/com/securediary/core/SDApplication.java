package com.securediary.core;

import com.securediary.access.DBAuthenticationService;
import com.securediary.access.UserAttsTable;
import com.securediary.access.UsersTable;
import com.securediary.calendar.CalendarGridProxyFactory;
import com.securediary.calendar.RecordEntity;
import com.securediary.command.*;
import com.securediary.scramble.Scrambler;
import com.securediary.scramble.ScramblerProxy;
import com.securediary.scramble.SimpleScrambler;
import com.securediary.tag.TagTable;
import com.securediary.tag.TagsService;
import org.ogai.command.sys.InitCommand;
import org.ogai.command.sys.LoginCommand;
import org.ogai.core.*;
import org.ogai.db.ConnectionsFactoryService;
import org.ogai.db.ConnectionsJNDIFactoryService;
import org.ogai.model.entity.EntityModel;

import javax.servlet.ServletContext;

/**
 * приложение Secure Diary
 *
 * @author Побединский Евгений
 *         29.03.14 10:45
 */
public class SDApplication {
	public static synchronized boolean init(ServletContext servletContext, String startPage, String mainPage){
		//Создаем системный контекст
		Ctx sysCtx = new Ctx();

		InitCommand.setInitialPage(startPage);
		LoginCommand.setMainPage(mainPage);

		boolean isApplicationStarted = Application.init(servletContext);

		if (isApplicationStarted) {
			try {
				//Специфичный код старта приложения
				//TODO remove later to another code - maybe to modules init
				ServicesRegistry services = ServicesRegistry.getInstance();
				services.register(AuthenticationService.NAME, new DBAuthenticationService());
				services.register(TagsService.NAME,  new TagsService());
				//Скремблер
				services.register(SimpleScrambler.NAME, new SimpleScrambler());
				services.register(ScramblerProxy.NAME, new ScramblerProxy());

				//Фавбрика соединений с БД
				//TODO вынести в application.properties
				services.register(ConnectionsFactoryService.NAME,
						//new ConnectionsJNDIFactoryService("java:comp/env/remoteTest")); //exception
						new ConnectionsJNDIFactoryService("java:comp/env/secureDiary")); //exception

				//Прочие объекты
				ObjectsRegistry objectsRegistry = ObjectsRegistry.getInstance();
				objectsRegistry.registerGridFactory(CalendarGridProxyFactory.NAME, new CalendarGridProxyFactory());

				RecordEntity recordsEntity = new RecordEntity();
				recordsEntity.register();
				objectsRegistry.register(new UsersTable());
				objectsRegistry.register(new UserAttsTable());
				objectsRegistry.register(new TagTable());
				objectsRegistry.register(new EntityModel(RecordEntity.NAME));

				//objectsRegistry.register(new EntityModel(clientsEntity.NAME));

				//Добавить запись
				CommandsRegistry.getInstance().register(ProcessNewRecordCommand.NAME,
						new ProcessNewRecordCommand());

				//Просмотреть существующий
				CommandsRegistry.getInstance().register(ViewRecordsCommand.NAME,
						new ViewRecordsCommand());

				//Обновить теги записи
				CommandsRegistry.getInstance().register(UpdateTagsCommand.NAME,
						new UpdateTagsCommand());

				CommandsRegistry.getInstance().register(GoMainPageCommand.NAME,
					new GoMainPageCommand());

				CommandsRegistry.getInstance().register(TagsListCommand.NAME,
						new TagsListCommand());

				CommandsRegistry.getInstance().register(ScrambleAllTagsCommand.NAME,
						new ScrambleAllTagsCommand());

			} catch (Exception e) {
				//Устанавливаем состояние приложения в Error и уничтожаем его
				Application.setError();

				Application.logErrorSafe("Error on securediary aplication init", e);
				Application.destroy();
				isApplicationStarted = false;
			}
		}

		sysCtx.destroy();
		return isApplicationStarted;
	}
}
