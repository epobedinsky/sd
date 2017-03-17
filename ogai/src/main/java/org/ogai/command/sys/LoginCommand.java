package org.ogai.command.sys;


import org.ogai.command.Executable;
import org.ogai.command.HTMLCommand;
import org.ogai.core.*;
import org.ogai.exception.OgaiException;
import org.ogai.text.OgaiText;
import org.ogai.util.Util;
import org.ogai.util.WebUtil;
import org.ogai.view.RedirectView;
import org.ogai.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Command - login user
 *
 * @author Побединский Евгений
 *         23.03.14 19:19
 */
public class LoginCommand extends HTMLCommand {
	public static final String NAME = "login";

	public static final String LOGIN_PARAM = "login";
	public static final String PASSWORD_PARAM = "password";
	public static final String LOGIN_PARAM_ERROR = "login_err";
	public static final String PASSWORD_PARAM_ERROR = "password_err";
	public static final String LOGIN_CMD_PARAM_ERROR = "login_cmd_err";

	private static String mainPage;

	/**
	 *
	 * @param mainPageArg Not empty main page name. Should be called only in synchronised code.
	 *                    Ideally once in application lifecycle
	 */
	public static void setMainPage(String mainPageArg) {
		mainPage = mainPageArg;
	}

	@Override
	public View doExecute(View view) throws OgaiException {
		Map<String, String> errors = new HashMap<String, String>();

		//Проверяем заполненность логина
		String login = Ctx.get().getRequestParams().get(LOGIN_PARAM);
		if (Util.isEmpty(login)) {
			errors.put(LOGIN_PARAM_ERROR, OgaiText.login_error_login.toString());
		}

		//Проверяем заполненность пароля
		String password = Ctx.get().getRequestParams().get(PASSWORD_PARAM);
		if (Util.isEmpty(password)) {
			errors.put(PASSWORD_PARAM_ERROR, OgaiText.login_error_password.toString());
		}

		AuthenticationService service =
				(AuthenticationService)ServicesRegistry.getInstance().get(AuthenticationService.NAME);

		User user = null;
		if (errors.isEmpty()) {
			user = service.loadUser(login, password);

			if (user == null) {
				errors.put(PASSWORD_PARAM_ERROR, OgaiText.user_not_found.toString());
			}
		}

		if (!errors.isEmpty()) {
			//Все ошибки передаем черезе параметры Ctx
			for (String key : errors.keySet()) {
				Ctx.get().getRequestParams().put(key, errors.get(key));
			}
			return CommandsRegistry.getInstance().get(InitCommand.NAME).execute();
		}

		service.loginUser(user);

		return new RedirectView(mainPage);
	}

	@Override
	protected boolean isAccess() {
		//Залогироваться можно всем
		return true;
	}
}
