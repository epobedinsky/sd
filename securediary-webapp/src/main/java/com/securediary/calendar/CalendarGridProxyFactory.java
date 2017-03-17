package com.securediary.calendar;

import com.securediary.command.GoMainPageCommand;
import com.securediary.command.ProcessNewRecordCommand;
import com.securediary.command.ViewRecordsCommand;
import com.securediary.scramble.ScrambledGridColumn;
import org.ogai.grid.*;
import org.ogai.grid.columns.*;
import org.ogai.model.CommandCall;
import org.ogai.model.SubmitCommandCall;
import org.ogai.util.Util;

/**
 * Фабрика прокси грида для списка записей в календаре
 *
 * @author Побединский Евгений
 *         06.04.14 22:59
 */
public class CalendarGridProxyFactory implements GridProxyFactory {
	public static final String NAME = "calendargrid";

	@Override
	public GridProxy create() {
		return new GridProxy(NAME) {
			@Override
			protected void defineGrid() {
				//final String DELETE_CMD_NAME = "delete_cmd";
				//final String DELETE_CMD_DISPLAY_VALUE = "Удалить";

				gridDifinition.addColumn(new GridColumn("id"));
				gridDifinition.addColumn(new GridColumn("scrambler"));
				gridDifinition.addColumn(new ScrambledGridColumn("title"));
				gridDifinition.addColumn(new DateColumn("create_date", Util.VIEW_DATETIME_FORMAT));

				//gridDifinition.addColumn(new ConstantColumn(DELETE_CMD_NAME, DELETE_CMD_DISPLAY_VALUE));

				//Операции
				CommandCall cmdCall = new SubmitCommandCall(GoMainPageCommand.NAME, SubmitCommandCall.Target.NEW);
				gridDifinition.addColumn(new CommandGridColumn("create_date", cmdCall, "id"));
				//CommandCall deleteCmd = new CommandCall(DeleteCommand.NAME);
				//deleteCmd.addParam(GoCommand.PARAM_NAME, ClientsEntity.NAME);
				//gridDifinition.addColumn(new CommandGridColumn(DELETE_CMD_NAME,
				//		deleteCmd, "id"));
			}

			@Override
			public GridDataSource getDataSource() {
				return new GridDBDataSource(RecordsQueries.calendar_grid.getQuery());
			}
		};
	}
}
