package com.securediary.access;

import org.ogai.core.ServicesRegistry;
import org.ogai.text.Text;
import org.ogai.text.TextService;

/**
 * Статические тексты связанные с доступом и аутентификацией
 *
 * @author Побединский Евгений
 *         06.04.14 16:38
 */
public enum AccessText implements Text {
	creator("Создатель"),
	admin("Администратор"),
	manager("Менеджер"),
	courier("Курьер"),
	federal("Федерал"),
	chief("Руководитель"),
	subagentManager("Менеджер субагента"),
	freelancer("Фрилансер")
	;

	private String defaultText;

	@Override
	public Object getText() {
		return defaultText;
	}

	@Override
	public String toString() {
		return ((TextService) ServicesRegistry.getInstance().get(TextService.NAME)).getText(this);
	}

	private AccessText(String defaultText) {
		this.defaultText = defaultText;
	}
}
