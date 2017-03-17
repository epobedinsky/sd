package org.ogai.view;

import org.ogai.exception.OgaiException;
import org.ogai.view.elements.Element;

/**
 * Абстракция вида - данных пересылаемых сервером для клиента
 *
 * @author Побединский Евгений
 *         23.03.14 17:41
 */
public abstract class View {
	/**
	 * Добавить элемент вида
	 */
	abstract public void add(Element element) throws OgaiException;
	/**
	 * рендерит вид для клиентав какой-то формат
	 */
	abstract public void render();
}
