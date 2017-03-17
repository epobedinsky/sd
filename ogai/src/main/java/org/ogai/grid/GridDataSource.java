package org.ogai.grid;

import org.ogai.exception.OgaiException;

/**
 * Интерфейс источника данных для грида
 *
 * @author Побединский Евгений
 *         06.04.14 23:12
 */
public interface GridDataSource {
	public GridData getData() throws OgaiException;
}
