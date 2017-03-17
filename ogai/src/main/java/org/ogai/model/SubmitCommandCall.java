package org.ogai.model;

import org.ogai.exception.OgaiException;
import org.ogai.util.Util;

/**
 * Вызов команды submit-отправки на сервер
 *
 * @author Побединский Евгений
 *         12.06.14 15:38
 */
public class SubmitCommandCall extends CommandCall {

	/**
	 * Где открывать результат перехода
	 */
	public enum Target {
		NEW("_blank"), //Новая вкладка
		THIS("_self"), //Эта вкладка
		TOP("_top"),   //Самое верхнее окно
		PARENT("_parent"); //Окно верхнего уровня

		public String toHtml() {
			return htmlCode;
		}

		private String htmlCode;

		private Target(String htmlCode) {
			this.htmlCode = htmlCode;
		}
	};

	private Target commandTarget;

	public SubmitCommandCall(String commandName, Target target) {
		super(commandName);

		commandTarget = target;
	}

	public Target getCommandTarget() {
		return commandTarget;
	}

	/**
	 * Принять посетителя для некоторой обработки этой комманды
	 * @param visitor
	 */
	public void accept(CommandCallVisitor visitor) throws OgaiException {
		visitor.visit(this);
	}
}
