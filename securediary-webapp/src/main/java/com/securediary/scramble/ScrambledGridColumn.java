package com.securediary.scramble;

import com.securediary.calendar.RecordEntity;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.grid.columns.GridColumn;

/**
 * Столюце для отображения заскремблированного столбца
 *
 * @author Побединский Евгений
 *         15.06.14 16:43
 */
public class ScrambledGridColumn extends GridColumn {
	/**
	 * @param name Не пустое имя столбца. Именно с таким именем значение из результатов запроса пойдет в грид
	 */
	public ScrambledGridColumn(String name) {
		super(name);
	}

	@Override
	public void process(QueryResult.Record srcRecord) throws OgaiException {
		Scrambler scrambler = ScramblerFactory.get((String)srcRecord.get(RecordEntity.COL_SCRAMBLER));
		srcRecord.put(getName(), scrambler.descramble((String)srcRecord.get(getName())));
	}
}
