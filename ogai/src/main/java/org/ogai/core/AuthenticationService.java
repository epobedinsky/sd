package org.ogai.core;

import org.ogai.exception.OgaiException;

/**
 * Interface for all possible authentication services
 *
 * @author Побединский Евгений
 *         23.03.14 19:45
 */
public abstract class AuthenticationService {
	public static final String NAME = "authentication";

	public abstract User loadUser(String login, String password) throws OgaiException;

	public void loginUser(User user) {
		Ctx.get().getRequest().getSession().setAttribute(Ctx.SESSION_PARAM_USER, user);
		Ctx newCtx = new Ctx(Ctx.get().getRequest(), Ctx.get().getResponse(), "login");
	}
}
