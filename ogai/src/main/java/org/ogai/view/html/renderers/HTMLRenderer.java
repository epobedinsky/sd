package org.ogai.view.html.renderers;

import org.ogai.exception.OgaiException;
import org.ogai.view.Renderer;
import org.ogai.view.elements.Element;

/**
 * Абстактный рендер для html
 *
 * @author Побединский Евгений
 *         12.04.14 12:51
 */
public abstract class HTMLRenderer<T extends Element> implements Renderer<T> {
	private StringBuilder sb;

	public HTMLRenderer(StringBuilder sb) {
		assert sb != null;

		this.sb = sb;
	}

	protected StringBuilder getSb() {
		return sb;
	}

	@Override
	public abstract void render(T element) throws OgaiException;

	@Override
	public String toString() {
		return sb.toString();
	}
}
