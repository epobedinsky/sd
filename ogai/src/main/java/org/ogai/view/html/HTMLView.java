package org.ogai.view.html;

import org.ogai.core.Ctx;
import org.ogai.core.ObjectsRegistry;
import org.ogai.exception.OgaiException;
import org.ogai.log.Log;
import org.ogai.log.LogFactory;
import org.ogai.view.View;
import org.ogai.view.elements.Element;

import java.io.IOException;

/**
 * Возвращение данных с сервера в виде HTML
 *
 * @author Побединский Евгений
 *         11.04.14 23:17
 */
public class HTMLView extends View {
	private static final Log log = LogFactory.create(HTMLView.class);
	private static final String HTML_CONTENT_TYPE = "text/html";
	private StringBuilder sb;
	private ObjectsRegistry objectsRegistry;

	public HTMLView() {
		this.sb = new StringBuilder();
		this.objectsRegistry = ObjectsRegistry.getInstance();
	}

	@Override
	public void add(Element element) throws OgaiException {
		assert element != null;

		log.debug("Add element: " + element);

		objectsRegistry.getHTMLRenderer(element.getName(), sb).render(element); //exception
	}

	@Override
	public void render() {
		try {
			log.debug("Start rendering HTMLView");
			log.debug("Result HTML:" + sb.toString());
			Ctx.get().getResponse().setContentType(HTML_CONTENT_TYPE);
			Ctx.get().getResponse().getWriter().write(sb.toString());
			Ctx.get().getResponse().flushBuffer();
			log.debug("Finished rendering HTMLView");
		} catch (IOException e) {
			log.error("Exception occured during HTMLView rendering:" + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
