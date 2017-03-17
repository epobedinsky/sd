package com.securediary.tag;

import org.ogai.core.ServicesRegistry;
import org.ogai.db.SQLQuery;
import org.ogai.db.StoredDBQuery;
import org.ogai.db.StoredDBQueryService;

/**
 * TODO Class Description
 *
 * @author Побединский Евгений
 *         12.06.14 17:11
 */
public enum TagsQueries  implements StoredDBQuery {
	all_tags("SELECT tag.id, tag.title, tag.scrambler, COUNT(tr.record_id) as records_count\n" +
			"FROM tag$tag tag INNER JOIN tag$tag_record tr ON tag.id = tr.tag_id\n" +
			"GROUP BY tag.id;"),
	record_tags("SELECT tag.id, tag.title, tag.scrambler\n" +
			"  FROM tag$tag tag INNER JOIN tag$tag_record tr ON tag.id = tr.tag_id WHERE tr.record_id = %s;"),
	create_record_tag_link("INSERT INTO tag$tag_record (tag_id, record_id)\n" +
			"    VALUES (%s, %s);"),
	delete_record_tag_link("DELETE FROM tag$tag_record WHERE tag_id = %s AND record_id = %s;")
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

	private TagsQueries(String defaultQuery) {
		this.defaultTQuery = defaultQuery;
	}
}
