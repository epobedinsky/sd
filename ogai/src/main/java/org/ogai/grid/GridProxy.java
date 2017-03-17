package org.ogai.grid;

import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.grid.columns.GridColumn;
import org.ogai.util.Util;

/**
 * Объект
 * содержащий переопределяемую логику по получению и обработке данных для грида
 * а также по настройке самого грида (пока не реализовано)
 *
 * @author Побединский Евгений
 *         06.04.14 20:53
 */
public abstract class GridProxy {
	private String name;

	//Описание грида
	protected GridDefinition gridDifinition;

	protected GridProxy(String name) {
		assert Util.isNotEmpty(name);

		this.name = name;
	}

	public GridData getData() throws OgaiException {
		//Настраиваем грид
		//TODO установка сортировки по умолчанию
		gridDifinition = new GridDefinition(name);
		defineGrid();

		//Получаем данные грида и обрабатываем
		GridDataSource ds = getDataSource();
		GridData gd = ds.getData();
		assert gd != null;

		//Преобразуем данные согласно настройкам грида
		//Для каждого столбца
		for (GridColumn column : gridDifinition.getColumns()) {
			//Выполняем обработку во всех строках
			for (QueryResult.Record record : gd.getQueryResult()) {
				column.process(record);
			}
		}

		processData(gd);
		return gd;
	}

	/**
	 * Определить грида
	 */
	protected abstract void defineGrid();

	/**
	 *
	 * @return Источник данных для грида
	 */
	protected abstract GridDataSource getDataSource();

	/**
	 * Оработать данные для грида gridData пред их отображением в гриде
	 * @param gridData не null
	 */
	protected void processData(GridData gridData) {

	}
}
