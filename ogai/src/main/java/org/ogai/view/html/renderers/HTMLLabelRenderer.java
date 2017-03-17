package org.ogai.view.html.renderers;

import org.ogai.exception.OgaiException;
import org.ogai.view.elements.LabelElement;

/**
 * Рендерер элемента с простым текстом LabelElement в HTML
 *
 * @author Побединский Евгений
 *         12.04.14 13:34
 */
public class HTMLLabelRenderer extends HTMLRenderer<LabelElement> {
	public HTMLLabelRenderer(StringBuilder sb) {
		super(sb);
	}

	@Override
	public void render(LabelElement element) throws OgaiException {
		getSb().append(element.getLabelText());
	}
}
