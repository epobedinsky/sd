package org.ogai.grid;

import org.ogai.grid.columns.GridColumn;
import org.ogai.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Описание и настройки грида
 *
 * @author Побединский Евгений
 *         10.04.14 20:46
 */
public class GridDefinition {
	private List<GridColumn> columns;

	private String name;

	/**
	 *
	 * @param name Непустое имя гридаю Совпадает с именем фабрики грид прокси его порождающей
	 */
	public GridDefinition(String name) {
		assert Util.isNotEmpty(name);

		this.columns = new ArrayList<GridColumn>();
		this.name = name;
	}

	/**
	 *
	 * @param gridColumn Ненулевой столбец грида
	 * @return this
	 */
	public GridDefinition addColumn(GridColumn gridColumn) {
		assert gridColumn != null;

		this.columns.add(gridColumn);
		return this;
	}

	/**
	 *
	 * @return Список столбцов
	 */
	public List<GridColumn> getColumns() {
		return columns;
	}
}
