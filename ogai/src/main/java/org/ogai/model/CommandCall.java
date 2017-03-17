package org.ogai.model;

import org.ogai.exception.OgaiException;
import org.ogai.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Описание вызова комманды
 *
 * @author Побединский Евгений
 *         10.04.14 23:13
 */
public class CommandCall {
	private String commandName;
	private Map<String,String> params;

	public CommandCall(String commandName) {
		assert Util.isNotEmpty(commandName);

		this.commandName = commandName;
		this.params = new HashMap<String, String>();
	}

	public String getCommandName() {
		return commandName;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public CommandCall addParam(String key, String value) {
		params.put(key, value);
		return this;
	}

	/**
	 * Принять посетителя для некоторой обработки этой комманды
	 * @param visitor
	 */
	public void accept(CommandCallVisitor visitor) throws OgaiException {
		visitor.visit(this);
	}
}
