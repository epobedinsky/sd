package org.ogai.model;

import org.ogai.exception.OgaiException;

/**
 * Исключение при отработке одного из методов Entity (т.е. при работе с сущностью)
 *
 * @author Побединский Евгений
 *         15.04.14 22:13
 */
public class EntityException extends OgaiException {
	public EntityException(Throwable e) {
		super(e);
	}

	public static enum Operation {
		LOAD,
		INSERT,
		UPDATE,
		DELETE
	};

	private Operation operation;

	/**
	 *
	 * @param operation Конкретная операция при выполнении которой возникла ошибка
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	@Override
	public String getMessage() {
		return operation + ":" +super.getMessage();
	}
}
