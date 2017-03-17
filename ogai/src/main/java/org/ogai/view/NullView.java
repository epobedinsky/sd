package org.ogai.view;

import org.ogai.view.elements.Element;

/**
 * Заглушка
 * На случай если в Action все напрямую пишется в reponse
 * что впрочем не рекомендуется
 *
 * @author Побединский Евгений
 *         30.03.14 23:05
 */
public class NullView extends View {


	@Override
	public void add(Element element) {
		//
	}

	@Override
	public void render() {

	}
}
