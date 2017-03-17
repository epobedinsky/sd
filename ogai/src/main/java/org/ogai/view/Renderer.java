package org.ogai.view;

import org.ogai.exception.OgaiException;
import org.ogai.view.elements.Element;

/**
 * интерфейс рендерера
 *
 * @author Побединский Евгений
 *         12.04.14 2:00
 */
public interface Renderer<T extends Element> {
	void render(T element) throws OgaiException;
}
