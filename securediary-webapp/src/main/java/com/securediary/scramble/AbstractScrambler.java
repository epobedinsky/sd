package com.securediary.scramble;

/**
 * Абстрактный скрэмблер
 *
 * @author Побединский Евгений
 *         15.06.14 16:24
 */
public abstract class AbstractScrambler implements Scrambler {
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractScrambler)) {
			return false;
		}
		return this.getName().equals(((AbstractScrambler)obj).getName());
	}
}
