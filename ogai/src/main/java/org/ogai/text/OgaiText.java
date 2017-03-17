package org.ogai.text;


import org.ogai.core.ServicesRegistry;

/**
 * Подпими в библиотеке Ogai
 *
 * @author Побединский Евгений
 *         05.04.14 19:00
 */
public enum OgaiText implements Text {
	login_error_login("Логин не задан"),
	login_error_password("Пароль не задан"),
	user_not_found("Неверный логин или пароль!"),
	no_access_page("У вас нет доступа к этой странице. Залогиньтесь")
	;

	private String defaultText;

	@Override
	public Object getText() {
		return defaultText;
	}


	@Override
	public String toString() {
		return ((TextService)ServicesRegistry.getInstance().get(TextService.NAME)).getText(this);
	}

	private OgaiText(String defaultText) {
		this.defaultText = defaultText;
	}
}
