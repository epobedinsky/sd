package org.ogai.grid;

import org.ogai.db.QueryResult;
import org.ogai.view.elements.Element;

/**
 * Элемент представления - данные грида
 *
 * @author Побединский Евгений
 *         08.04.14 20:01
 */
public class GridDataElement extends Element {
	public static final String NAME = "griddata";

	private Integer pageNumber;
	private Integer totalPagesCount;
	private QueryResult rows;

	@Override
	public String getName() {
		return NAME;
	}

	public GridDataElement(Integer pageNumber, Integer totalPagesCount) {
		setPageNumber(pageNumber);
		setTotalPagesCount(totalPagesCount);
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		assert pageNumber != null : "pageNumber is null";

		this.pageNumber = pageNumber;
	}

	public Integer getTotalPagesCount() {
		return totalPagesCount;
	}

	public void setTotalPagesCount(Integer totalPagesCount) {
		assert totalPagesCount != null : "totalPagesCount is null";

		this.totalPagesCount = totalPagesCount;
	}

	public QueryResult getRows() {
		return rows;
	}

	public void setRows(QueryResult rows) {
		assert rows != null : "rows is null";

		this.rows = rows;
	}

	@Override
	public String toString() {
		return "GridDataElement{" +
				"pageNumber=" + pageNumber +
				", totalPagesCount=" + totalPagesCount +
				", rows=" + rows +
				'}';
	}
}
