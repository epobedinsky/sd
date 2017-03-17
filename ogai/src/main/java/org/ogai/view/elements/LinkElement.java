package org.ogai.view.elements;

import org.ogai.model.CommandCall;

/**
 * Элемент - вызов действия через ссылку <a href
 *
 * @author Побединский Евгений
 *         11.04.14 22:20
 */
public class LinkElement extends Element {
	public static final String NAME = "link";

	private CommandCall commandCall;
	private LabelElement labelElement;

	public LinkElement(CommandCall commandCall, LabelElement label) {
		this.commandCall = commandCall;
		this.labelElement = label;
	}

	public CommandCall getCommandCall() {
		return commandCall;
	}

	@Override
	public String getName() {
		return NAME;
	}

	public LabelElement getLabelElement() {
		return labelElement;
	}
}
