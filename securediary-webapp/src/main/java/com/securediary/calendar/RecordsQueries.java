package com.securediary.calendar;

import org.ogai.core.ServicesRegistry;
import org.ogai.db.SQLQuery;
import org.ogai.db.StoredDBQuery;
import org.ogai.db.StoredDBQueryService;

/**
 * Запросы для получения записей
 *
 * @author Побединский Евгений
 *         06.04.14 23:41
 */
public enum RecordsQueries implements StoredDBQuery {
	//TODO сейчас отображаются записи только из одного дневника
	calendar_grid("SELECT id, create_date, title, scrambler\n" +
			"  FROM rec$record WHERE diary_id = 0 ORDER BY create_date DESC;"),

	view_records("SELECT record_id,order_num,page_content,create_date, title, scrambler FROM rec$record rec\n" +
			"INNER JOIN  rec$recordpage rp ON rec.id = rp.record_id\n" +
			"WHERE record_id IN(%s)\n" +
			"ORDER BY create_date DESC, record_id DESC, order_num ASC")
	;

	private String defaultTQuery;

	@Override
	public Object getText() {
		return defaultTQuery;
	}


	@Override
	public SQLQuery getQuery() {
		return ((StoredDBQueryService) ServicesRegistry.getInstance().get(StoredDBQueryService.NAME)).getQueryText(this);
	}

	private RecordsQueries(String defaultQuery) {
		this.defaultTQuery = defaultQuery;
	}
}
