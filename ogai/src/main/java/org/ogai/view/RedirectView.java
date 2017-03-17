package org.ogai.view;

import org.ogai.core.Application;
import org.ogai.core.Ctx;
import org.ogai.util.Util;
import org.ogai.util.WebUtil;
import org.ogai.view.elements.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * View that redirects user to another
 *
 * @author Побединский Евгений
 *         23.03.14 17:43
 */
public class RedirectView extends View {
	private String url;
	private String addParams;

	public RedirectView(String url) {
		this.url = url;
		this.addParams = null;
	}

	public RedirectView(String url, String addParams) {
		this(url);
		this.addParams = "?" + WebUtil.decodeURL(addParams, WebUtil.WEB_CODING);
	}

	@Override
	public void add(Element element) {
		//
	}

	@Override
	public void render() {
		try {
			String finalUrl = !Util.isEmpty(this.addParams) ? WebUtil.decodeURL(url, WebUtil.WEB_CODING) + addParams
					: WebUtil.decodeURL(url, WebUtil.WEB_CODING);

			Application.logInfoSafe("Redirect to:" + finalUrl);
			this.getRequest().getRequestDispatcher(finalUrl).forward(this.getRequest(), this.getResponse());
		} catch (Exception e) {
			Application.logErrorSafe("Error rendering RegirectView: finalUrl = " + url
					+ ", addParams = " + addParams, e);
		}
	}

	protected void setAddParams(String addParams) {
		this.addParams = addParams;
	}

	//перегрузить эти методы, если нужно брать request и response из других источников
	protected HttpServletRequest getRequest() {
		return Ctx.get().getRequest();
	}

	protected HttpServletResponse getResponse() {
		return Ctx.get().getResponse();
	}
}

