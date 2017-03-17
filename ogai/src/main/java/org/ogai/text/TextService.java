package org.ogai.text;

/**
 * Сервис для получение статических текстов
 *
 * @author Побединский Евгений
 *         05.04.14 19:22
 */
public class TextService {
	public static final String NAME = "text";

	public String getText(Text text) {
		return (String)text.getText();
	}
}
