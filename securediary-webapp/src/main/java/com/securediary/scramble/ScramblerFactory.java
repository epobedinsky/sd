package com.securediary.scramble;

import org.ogai.core.ServicesRegistry;
import org.ogai.util.Util;

/**
 * Фабрика скрэмблеров
 *
 * @author Побединский Евгений
 *         15.06.14 16:19
 */
public class ScramblerFactory {
	public static final Scrambler get(String name) {
		name = (Util.isEmpty(name)) ? ScramblerProxy.NAME : name;

		return (Scrambler) ServicesRegistry.getInstance().get(name);
	}
}
