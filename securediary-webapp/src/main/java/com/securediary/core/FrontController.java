package com.securediary.core;

import org.ogai.command.HTMLCommand;
import org.ogai.core.*;
import org.ogai.exception.OgaiException;
import org.ogai.log.Log;
import org.ogai.log.LogFactory;
import org.ogai.log.LogService;
import org.ogai.util.WebUtil;
import org.ogai.view.ErrorView;
import org.ogai.view.View;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * The main entry-point for all web-request of entire application
 *
 * @author Побединский Евгений
 *         20.03.14 22:53
 */
public class FrontController extends HttpServlet {
	public static final String SERVLET_EXTENSION = ".do";
	public static final String DEFAULT_PAGE = "login.jsp";
	public static final String MAIN_PAGE = "main.jsp";

	private static Log log = LogFactory.create(FrontController.class);

	private ServletContext servletContext;

	@Override
	public void init(ServletConfig config) throws ServletException {
		servletContext = config.getServletContext();
		super.init(config);
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Prepare request and response
		prepare(req, resp);

		//Initial application settings if needed
		View view = null;
		if (!Application.isInitialized()) {
			if (!SDApplication.init(servletContext, DEFAULT_PAGE, MAIN_PAGE)){
				//Перенаправляем на errorScreen
				view = getErrorView(req, resp, "Приложение не смогло стартовать", null);
			}
		}

		if (Application.isInitialized()){
			//Create context
			String commandName = getCommandName(req);
			Ctx ctx = new Ctx(req, resp, commandName);

			log.debug("------------------[ACTION BEGIN]--------------------------");
			log.debug(WebUtil.getUrlString(req));


			try {
				//Get and execute command
				view = CommandsRegistry.getInstance().get(commandName).execute();
			} catch(OgaiException oe) {
				log.error("Error occured during request handling:" + ctx.getRequestParams() + "; "
						+ ctx + " : " + oe.getMessage(), oe);

				view = getErrorView(null, oe);
			} catch(Exception e) {
				log.error("Unknown error occured during request handling:" + ctx.getRequestParams() + "; "
						+ ctx + " : " + e.getMessage(), e);

				view = getErrorView(null, null);
			}
		}
		//render view
		view.render();
		logActionFinish();
	}

	private void logActionFinish() {
		//Логгер может быть не создан тут
		if (ServicesRegistry.getInstance().has(LogService.NAME)) {
			log.debug("------------------[ACTION FINISH]--------------------------");
		} else {
			//TODO: писать в лог томката
			System.out.println("------------------[ACTION FINISH]--------------------------");
		}
	}

	private String getCommandName(HttpServletRequest req) {
		String servletPath = req.getServletPath();
		int commandNameDelimiterIndex = servletPath.lastIndexOf(SERVLET_EXTENSION);
		return servletPath.substring(1, commandNameDelimiterIndex);
	}

	private void prepare(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
		req.setCharacterEncoding("UTF-8");
		resp.setDateHeader("Expires", -1);
		resp.setHeader("Pragma", "no-cache");
		resp.setCharacterEncoding("UTF-8");
		if (req.getProtocol().equals("HTTP/1.1")) {
			resp.setHeader("Cache-Control", "no-cache");
		}
	}

	private View getErrorView(String message, OgaiException e) {
		return new ErrorView(HTMLCommand.ERROR_PAGE, message, e);
	}

	//Показываем ErrorView когдa контект еще не готов
	private View getErrorView(final HttpServletRequest request, final HttpServletResponse response, String message, OgaiException e) {
		return new ErrorView(HTMLCommand.ERROR_PAGE, message, e) {
			@Override
			protected HttpServletRequest getRequest() {
				return request;
			}

			@Override
			protected HttpServletResponse getResponse() {
				return response;
			}
		};
	}

//	@Override
//	public void destroy() {
//		Application.destroy();
//	}
}
