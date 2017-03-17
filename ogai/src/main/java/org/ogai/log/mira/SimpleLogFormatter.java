package org.ogai.log.mira;


import org.ogai.core.Application;
import org.ogai.core.Ctx;
import org.ogai.util.Util;

public class SimpleLogFormatter implements LogFormatter {

	@Override
	public String getMessage(String type, String name, String desc) {
		return appendTypeAndNameAndDesc(appendThreadAndTime(new StringBuilder()).append(getCurrentUser()).append(" "), type, name, desc).toString();
	}

	/**
	 * Добавить часть, которая содержит имя потока и время
	 *
	 * @return переданный буффер строк с добавленной информацией
	 */
	protected StringBuilder appendThreadAndTime(StringBuilder sb) {
		assert sb != null : "StringBuffer should not be null";
		sb.append(Thread.currentThread().getName()).append(" ");
		sb.append(Util.formatNow()).append(" ");
		return sb;
	}

	/**
	 * Добавить часть, которая содержит тип дебага, имя класса и описание
	 *
	 * @return переданный буффер строк с добавленной информацией
	 */
	protected StringBuilder appendTypeAndNameAndDesc(StringBuilder sb, String type, String name, String desc) {
		assert sb != null : "StringBuffer should not be null";
		sb.append(type);
		sb.append('(').append(name).append("): ");
		sb.append(desc);
		return sb;
	}

	protected String getCurrentUser() {
		if (!Application.isInitialized()) {
			return "INIT";
		} else if (Ctx.get().isSystem()) {
			return "SYSTEM";
		} else if (null == Ctx.get().getUser()) {
			return "EMPTY";
		} else if (Ctx.get().isGuest()) {
			return "guest";
		}

		return Ctx.get().getUser().getLogin();
	}
}

