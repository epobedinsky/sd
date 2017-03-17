package org.ogai.model;

import org.ogai.exception.OgaiException;

/**
 * Посетитель различных видов вызовов комманд
 *
 * @author Побединский Евгений
 *         11.04.14 23:06
 */
public interface CommandCallVisitor {
	/**
	 * Посетить обычную комманду
	 * @param acceptor
	 */
	void visit(CommandCall acceptor) throws OgaiException;

	/**
	 * Посетить комманду перехода на экран
	 * @param acceptor
	 */
	void visit(SubmitCommandCall acceptor)throws OgaiException;
}
