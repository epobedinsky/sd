package com.securediary.scramble;

/**
 * Всопомгательные методы для скрэмблинга
 *
 * @author Побединский Евгений
 *         15.06.14 17:04
 */
public class ScrambleUtil {
	public static String descramble(String src, String scramblerName) {
		return ScramblerFactory.get(scramblerName).descramble(src);
	}
}
