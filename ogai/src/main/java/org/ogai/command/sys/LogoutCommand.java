package org.ogai.command.sys;

import org.ogai.command.Executable;
import org.ogai.core.Ctx;
import org.ogai.exception.OgaiException;
import org.ogai.view.RedirectView;
import org.ogai.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Log out user
 *
 * @author Побединский Евгений
 *         23.03.14 21:02
 */
public class LogoutCommand implements Executable {
	public static final String NAME = "logout";

	@Override
	public View execute()  throws OgaiException {
		Ctx ctx = Ctx.get();
		HttpServletRequest request = ctx.getRequest();
		HttpServletResponse response = ctx.getResponse();
		Ctx.get().destroy();

		//Создаем новый гостевой контекст
		@SuppressWarnings("UnusedDeclaration")
		Ctx newGuestContext = new Ctx(request, response, InitCommand.NAME);

		return new RedirectView(InitCommand.getInitialPage());
	}
}
