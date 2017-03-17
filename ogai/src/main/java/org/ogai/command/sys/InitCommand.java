package org.ogai.command.sys;

import org.ogai.command.Executable;
import org.ogai.core.Ctx;
import org.ogai.exception.OgaiException;
import org.ogai.util.WebUtil;
import org.ogai.view.RedirectView;
import org.ogai.view.View;

/**
 * Just regirect user to default application page
 *
 * @author Побединский Евгений
 *         23.03.14 18:17
 */
public class InitCommand implements Executable {
	public static final String NAME = "init";


	private static String initialPage;

	/**
	 *
	 * @param initialPageArg Not empty initial page name. Should be called only in synchronised code.
	 *                    Ideally once in application lifecycle
	 */
	public static void setInitialPage(String initialPageArg) {
		initialPage = initialPageArg;
	}

	public static String getInitialPage() {
		return initialPage;
	}


	@Override
	public View execute() throws OgaiException {
		return new RedirectView(initialPage, WebUtil.getUrlParamsString(Ctx.get().getRequestParams()));
	}
}
