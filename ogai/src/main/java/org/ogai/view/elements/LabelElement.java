package org.ogai.view.elements;

/**
 * Вывод простого текста
 *
 * @author Побединский Евгений
 *         12.04.14 1:49
 */
public class LabelElement extends Element {
	public static final String NAME = "label";

	private String labelText;

	public LabelElement(String labelText) {
		this.labelText = labelText;
	}

	@Override
	public String getName() {
		return NAME;
	}

	public String getLabelText() {
		return labelText;
	}
}
