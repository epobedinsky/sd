package org.ogai.core;

import org.ogai.command.sys.InitCommand;
import org.ogai.db.DBSession;
import org.ogai.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Execution context
 *
 * @author Побединский Евгений
 *         22.03.14 21:41
 */
public class Ctx {
	public enum CtxType {
		GUEST, SYSTEM, ORDINAL
	}

	public static final String CMD_NAME = "cmdname";

	private static final String SESSION_PARAM_HISTORY = "sp_history";
	public static final String SESSION_PARAM_USER = "sp_user";

	//To ensure access to Ctx from every point of current thread
	static ThreadLocal<Ctx> thread = new ThreadLocal<Ctx>();

	private User user;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	//true if current user is not logged in
	private CtxType type;
	private History history;
	private Map<String, String> requestParams;

	//Текущая сессия работы пользователя в БД, если есть
	//если нет - null
	private DBSession currentDBSession;

	/**
	 * Создать системный контекст
	 */
	public Ctx() {
		type = CtxType.SYSTEM;

		if (thread.get() != null) {
			thread.remove();
		}
		thread.set(this);
	}

	public Ctx(HttpServletRequest request, HttpServletResponse response, String commandName) {
		this.request = request;
		this.response = response;
		this.session = request.getSession(false);
		this.requestParams = toRequestParams(request, commandName);

		//If session is null - user not logged
		boolean isGuest = (session == null) || (session.getAttribute(SESSION_PARAM_USER) == null);
		if (!isGuest){
			history = (History)session.getAttribute(SESSION_PARAM_HISTORY);
			if (history == null) {
				history = new History();
				session.setAttribute(SESSION_PARAM_HISTORY, history);
			}
			user = (User)session.getAttribute(SESSION_PARAM_USER);
		} else if (session != null) {
			session.invalidate();
		}

		if (isGuest) {
			type = CtxType.GUEST;
		} else {
			type = CtxType.ORDINAL;
		}

		if (thread.get() != null) {
			thread.remove();
		}
		thread.set(this);
	}

	public static Ctx get() {
		Ctx ctx = thread.get();
		if (ctx == null) {
			throw new IllegalStateException("Context is not created");
		}

		return ctx;
	}

	public void destroy() {
		if (!isSystem() && (request.getSession(false) != null)) {
			request.getSession().invalidate();
		}
		thread.remove();
	}

	public User getUser() {
		return user;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpSession getSession() {
		return session;
	}

	public History getHistory() {
		return history;
	}

	public Map<String, String> getRequestParams() {
		return requestParams;
	}

	public DBSession getCurrentDBSession() {
		return currentDBSession;
	}

	public void setCurrentDBSession(DBSession currentDBSession) {
		assert currentDBSession != null : "currentDBSession is null";

		this.currentDBSession = currentDBSession;
	}

	/**
	 * Очистить текущую сессию работы пользователя с БД
	 */
	public void clearCurrentDBSession() {
		this.currentDBSession = null;
	}

	public boolean isGuest() {
		return type == CtxType.GUEST;
	}

	public boolean isSystem() {
		return type == CtxType.SYSTEM;
	}

	@Override
	public String toString() {
		if (isSystem()) {
			return "Ctx{System}";
		}
		return "Ctx{" +
				"user=" + (isGuest()? "guest" : user) + "," +
				"requestParams=" + requestParams +
				'}';
	}

	private Map<String,String> toRequestParams(HttpServletRequest request, String commandName) {
		Map<String, String> result = WebUtil.getParams(request);
		result.put(CMD_NAME, commandName);
		return result;
	}
}
