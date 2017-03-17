package org.ogai.command;

import org.ogai.command.sys.GoCommand;
import org.ogai.command.sys.InitCommand;
import org.ogai.command.sys.LoginCommand;
import org.ogai.core.CommandsRegistry;
import org.ogai.core.Ctx;
import org.ogai.core.History;
import org.ogai.exception.OgaiException;
import org.ogai.text.OgaiText;
import org.ogai.util.Util;
import org.ogai.view.ErrorView;
import org.ogai.view.View;

/**
 * Комманда срабатывающая в ответ на переход по ссылке или отправку форму через POST
 * и возвращающая на клиент HTML - страницу
 *
 * Требует перепоределения метода doExecute
 *
 * @author Побединский Евгений
 *         12.04.14 22:26
 */
public abstract class HTMLCommand extends AbstractCommand {
	public static final String ERROR_PAGE = "error.jsp";

	@Override
	protected View getErrorView(OgaiException e) {
		return new ErrorView(ERROR_PAGE, null, e);
	}

	@Override
	protected View getErrorView(Exception e) {
		return new ErrorView(ERROR_PAGE, null, null);
	}

	@Override
	protected View noAccessView() {
		try {
			Ctx.get().getRequestParams().put(LoginCommand.LOGIN_CMD_PARAM_ERROR,
					OgaiText.no_access_page.toString());

			return CommandsRegistry.getInstance().get(InitCommand.NAME).execute();
		} catch (OgaiException e) {
			log.error("Error sending to no - access view:" + e.getMessage(), e);
			return new ErrorView(ERROR_PAGE, null, e);
		}
	}

	/**
	 * @param id - Любой id который используется если тот, что в Step null (для операции создания новой сущности)
	 * Перегружает текущий экран
	 * @return  View  - null если переходить еще некуда
	 */
	protected View reloadCurrentWindow(String id) throws OgaiException {
		History.HistoryStep step = Ctx.get().getHistory().reloadStep();
		if (step != null) {
			return redirectGo(step, id);
		}

		return null;
	}

	private View redirectGo(History.HistoryStep step, String id) throws OgaiException {
		assert step != null;

		try {
			//Готовим параметры для перехода
			Ctx.get().getRequestParams().put(GoCommand.PARAM_ID, Util.isEmpty(step.getId()) ? id : step.getId());
			//Переходим
			Ctx.get().getRequestParams().put(GoCommand.PARAM_NAME, step.getName());

			return CommandsRegistry.getInstance().get(GoCommand.NAME).execute();
		} catch (CommandsRegistry.UnknownCommandException e) {
			log.error("Unpossible error:" + e.getMessage());
			throw new OgaiException(e);
		}
	}
}
