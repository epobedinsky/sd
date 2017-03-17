package org.ogai.view;

import org.ogai.exception.OgaiException;
import org.ogai.util.Util;

/**
 * Отображение ошибки для пользователя
 *
 * @author Побединский Евгений
 *         29.03.14 16:05
 */
public class ErrorView extends RedirectView {
	public static final String RP_ERROR_DETAILS = "errorDetails";

	public static final String ERROR_MESSAGE_DELIMITER = ".<br/>";


	public ErrorView(String errorScreenUrl, String message, OgaiException e) {
		super(errorScreenUrl);

		String finalMessage = Util.isNotEmpty(message) ? message : "";
		if (null != e && Util.isNotEmpty(e.getUserDisplayMessage())) {
			if (Util.isNotEmpty(finalMessage)) {
				finalMessage.concat(ERROR_MESSAGE_DELIMITER);
			}

			finalMessage = finalMessage.concat(e.getUserDisplayMessage());
		}

		if (Util.isNotEmpty(finalMessage)) {
			setAddParams("?" + RP_ERROR_DETAILS + "=" + finalMessage);
		}
	}
}
