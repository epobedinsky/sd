package org.ogai.view.elements;

/**
 * Абстрактный элемент вида
 *
 * @author Побединский Евгений
 *         07.04.14 23:01
 */
public abstract class Element {
	/**
	 *
	 * @return Уникальное имя элемента (для  нахождения для элемента уникального рендерера)
	 */
	public abstract String getName();
}
