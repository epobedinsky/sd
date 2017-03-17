package com.securediary.scramble;

/**
 * Заменитель скрэмблера, который используется если никакой скрэмблер не используется
 *
 * @author Побединский Евгений
 *         15.06.14 16:17
 */
public class ScramblerProxy extends AbstractScrambler {
	public static final String NAME = "proxy";

	@Override
	public String scramble(String src) {
		return src;
	}

	@Override
	public String descramble(String src) {
		return src;
	}

	@Override
	public String getName() {
		return NAME;
	}
}
