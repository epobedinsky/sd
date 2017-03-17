package org.ogai.grid.columns;

import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;

/**
 * Колонка грида отобрадающая HTML шаблон заполненый данными из
 * queryResult.
 *
 * //TODO в идеале нужно разделить порядок обработки и порядок отображения в гриде
 * Если нужно чтоб в шаблон подставлялись уе преобразованные значения этот параметр нужно расположить послежним
 *
 * @author Побединский Евгений
 *         18.04.14 22:49
 */
public class HTMLColumn extends GridColumn {
	private String htmlPattern;

	/**
	 * @param name Не пустое имя столбца. Именно с таким именем значение из результатов запроса пойдет в грид
	 * @param htmlPattern в виде <H1>{res1} hey {res2} !!{res1} </H1>
	 */
	public HTMLColumn(String name, String htmlPattern) {
		super(name);
		this.htmlPattern = htmlPattern;
	}

	@Override
	public void process(QueryResult.Record srcRecord) throws OgaiException {
		String result = htmlPattern;
		for (String column : srcRecord.keySet()) {
			result = result.replaceAll("_%" + column + "%_", String.valueOf(srcRecord.get(column)));
		}
		srcRecord.put(getName(), result);
	}
}
