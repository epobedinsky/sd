package org.ogai.grid.columns;

import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.model.GoCommandCall;

/**
 * Столбец с командами перехода на экраны редактирования для каждой строки из грида.
 * Добавлять в GridDefinition после всех простых стобцов с данными!!!
 *
 * @author Побединский Евгений
 *         12.04.14 14:55
 */
public class GoCommandGridColumn extends CommandGridColumn {
	private String idColumnName;

	/**
	 * @param name          Не пустое имя столбца. Именно с таким именем значение из результатов запроса пойдет в грид
	 * @param screenName    Имя экрана на который будет осуществляться переход
	 * @param idColumnName, имя колонки из результатов запроса, значение из которой будет id cущности
	 * @param argColumnNames имена колонок откуда взять прочие аттрибуты для передачи в GoCommand
	 *                         которую отобразим на этом экране
	 */
	public GoCommandGridColumn(String name, String screenName, String idColumnName, String... argColumnNames) {
		super(name, new GoCommandCall(screenName, GoCommandCall.Target.NEW), argColumnNames);
		this.idColumnName = idColumnName;
	}

	@Override
	public void process(QueryResult.Record srcRecord) throws OgaiException {
		assert srcRecord.containsKey(idColumnName);
		assert srcRecord.get(idColumnName) != null;

		//Добавить в параметры вызова id
		getCommandCall().setId(srcRecord.get(idColumnName).toString());

		super.process(srcRecord);
	}

	@Override
	protected GoCommandCall getCommandCall() {
		return (GoCommandCall)super.getCommandCall();
	}
}
