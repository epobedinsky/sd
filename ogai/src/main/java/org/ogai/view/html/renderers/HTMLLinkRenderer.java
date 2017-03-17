package org.ogai.view.html.renderers;

import org.ogai.core.ObjectsRegistry;
import org.ogai.exception.OgaiException;
import org.ogai.model.CommandCall;
import org.ogai.model.CommandCallVisitor;
import org.ogai.model.GoCommandCall;
import org.ogai.model.SubmitCommandCall;
import org.ogai.util.WebUtil;
import org.ogai.view.elements.LabelElement;
import org.ogai.view.elements.LinkElement;

import java.io.UnsupportedEncodingException;

/**
 * Рендерер LinkElement в html
 * Формирует анкерную ссылку <a href=...></a>
 *
 * @author Побединский Евгений
 *         12.04.14 1:56
 */
public class HTMLLinkRenderer extends HTMLRenderer<LinkElement> implements CommandCallVisitor {

	public HTMLLinkRenderer(StringBuilder sb) {
		super(sb);
	}

	@Override
	public void render(LinkElement element) throws OgaiException {
		super.getSb().append("<a ");
		element.getCommandCall().accept(this); //exception
		super.getSb().append(">");

		renderBody(element.getLabelElement()).append("</a>"); //exception
	}

	@Override
	public void visit(CommandCall acceptor) throws OgaiException {
		renderCommandCall(acceptor); //exception
	}

	@Override
	public void visit(SubmitCommandCall acceptor) throws OgaiException {
		//Рендерим target - способ открытия (в этой вкладке, в новой и т.д.)
		super.getSb().append(" target = '").append(acceptor.getCommandTarget().toHtml()).append("'");
		renderCommandCall(acceptor);
	}

	private void renderCommandCall(CommandCall cmd) throws OgaiException {
		try {
			super.getSb().append(" href = '")
					.append(cmd.getCommandName() + ".do?" + WebUtil.getUrlParamsString(cmd.getParams(), true))
					.append("'");
		} catch (UnsupportedEncodingException e) {
			throw new OgaiException("Error rendering command call", e);
		}
	}

	private StringBuilder renderBody(LabelElement element) throws OgaiException {
		//Берем рендерер для лейла
		HTMLRenderer renderer = ObjectsRegistry.getInstance().getHTMLRenderer(element.getName(), super.getSb());

		//Ренедрим
		renderer.render(element);

		return super.getSb();
	}
}
