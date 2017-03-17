package org.ogai.model;

import org.ogai.command.sys.GoCommand;
import org.ogai.exception.OgaiException;
import org.ogai.util.Util;

/**
 * Вызов команды перехода
 *
 * @author Побединский Евгений
 *         10.04.14 23:27
 */
public class GoCommandCall extends SubmitCommandCall {



	public GoCommandCall(String id, String name, Target target) {
		super(GoCommand.NAME, target);

		assert Util.isNotEmpty(name);
		assert Util.isNotEmpty(id);

		super.addParam(GoCommand.PARAM_ID, id);
		super.addParam(GoCommand.PARAM_NAME, name);
	}

	public GoCommandCall(String name, Target target) {
		super(GoCommand.NAME, target);

		assert Util.isNotEmpty(name);

		super.addParam(GoCommand.PARAM_NAME, name);
	}

	/**
	 *
	 * @param id Не пустое. id сущности которая будет отображена на экране на который переходим
	 */
	public void setId(String id) {
		super.addParam(GoCommand.PARAM_ID, id);
	}

	@Override
	public void accept(CommandCallVisitor visitor)throws OgaiException {
		visitor.visit(this);
	}
}
