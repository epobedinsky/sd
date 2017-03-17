package org.ogai.command;

import org.ogai.exception.OgaiException;
import org.ogai.view.View;

/**
 * интерфейс для комманд - то, что может быть выполнено т.е. содержит метод Execute
 *
 * @author Побединский Евгений
 *         29.03.14 18:32
 */
public interface Executable {
	public View execute() throws OgaiException;
}
