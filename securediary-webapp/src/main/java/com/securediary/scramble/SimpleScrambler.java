package com.securediary.scramble;

import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.ogai.core.Application;
import org.ogai.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Поростой скрэмблер
 *
 * @author Побединский Евгений
 *         14.06.14 16:25
 */
public class SimpleScrambler extends AbstractScrambler {
	public static final String NAME = "simple";

	private static final String NORMAL =
			"абвгдеёжзийклмнопрстуфхцчшщьыъэюя"
			+ ",:.АБВГДЕЁЖЗИЙКЛМН!-ОПРСТУФХЦЧШЩЬЫЪЭЮЯ"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz—?";

	private static final String KEY =
			"эх, пиши: зять съел щи, яйцо, чан брюквы. ждём фигу!"
					+ "THE FIVE BOXING WIZARDS JUMP QUICKLY"
					+ "jackdaws love my big sphinx of quartz?"
					+ "- ЛЮБЯ, СЪЕШЬ ЩИПЦЫ, - ВЗДОХНЁТ МЭР, — КАЙФ ЖГУЧ";


	private Character[] normalArray;
	private Map<Character, Integer> normalMap;

	private Character[] keyArray;
	private Map<Character, Integer> keyMap;

	public SimpleScrambler() {
		normalMap = new HashMap<Character, Integer>();
		normalArray = init(NORMAL, normalMap);

		keyMap = new HashMap<Character, Integer>();
		keyArray = init(KEY, keyMap);
	}

	@Override
	public String scramble(String src) {
		return transform(src, NORMAL, KEY);
	}

	@Override
	public String descramble(String src) {
		return transform(src, KEY, NORMAL);
	}

	@Override
	public String getName() {
		return NAME;
	}

	private String transform(String src, String srcKey, String dstKey) {
		if (Util.isEmpty(src)) {
			return src;
		}

		Map<Character, Integer> map = srcKey.equals(NORMAL) ? normalMap : keyMap;
		Character[] array = dstKey.equals(NORMAL) ? normalArray : keyArray;

		StringBuilder sb = new StringBuilder();
		for (char nextChar : src.toCharArray()) {
			if (map.containsKey(nextChar)) {
				sb.append(array[map.get(nextChar)]);
			} else {
				sb.append(nextChar);
			}
		}

		return sb.toString();
	}

	private Character[] init(String raw, Map<Character, Integer> map) {
		List<Character> temp = new ArrayList<Character>();

		int counter = 0;
		for (Character nextChar : raw.toCharArray()) {
			if (!nextChar.equals(' ') && !map.containsKey(nextChar)) {
				map.put(nextChar, counter);
				temp.add(nextChar);
				counter++;
			}
		}

		return temp.toArray(new Character[0]);
	}
}
