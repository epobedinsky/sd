package com.securediary.scramble;

/**
 * Интерфейс скрэмблера
 *
 * @author Побединский Евгений
 *         14.06.14 16:24
 */
public interface Scrambler {
	public String scramble(String src);

	public String descramble(String src);

	public String getName();
}
