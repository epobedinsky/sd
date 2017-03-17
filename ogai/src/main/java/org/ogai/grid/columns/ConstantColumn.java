package org.ogai.grid.columns;

import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;

/**
 * Постоянное значение в столбце для всех строк
 *
 * @author Побединский Евгений
 *         12.04.14 17:42
 */
public class ConstantColumn extends GridColumn {
	//Постояное значение добавляемое в Грид
	private final Object value;

	/**
	 * @param name Не пустое имя столбца. Именно с таким именем значение из результатов запроса пойдет в грид
	 * @param value Постояное значение добавляемое в Грид
	 */
	public ConstantColumn(String name, Object value) {
		super(name);
		this.value = value;
	}

	@Override
	public void process(QueryResult.Record srcRecord) throws OgaiException {
		srcRecord.put(getName(), value);
	}
}
