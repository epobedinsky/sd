package com.securediary.tag;

import com.securediary.scramble.Scrambler;
import com.securediary.scramble.ScramblerFactory;
import org.ogai.core.ObjectsRegistry;
import org.ogai.db.DBSession;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;

import java.util.*;

/**
 * Операции с тэгами
 *
 * @author Побединский Евгений
 *         12.06.14 16:33
 */
public class TagsService {

	public static final String TAGS_DELIMITER = ",";
	public static final String NAME = "tags";

	/**
	 * Получить по всем тегам полную информацию
	 */
	public List<FullTag> getAllTags() throws OgaiException {
		QueryResult qr = DBSession.selectQuery(TagsQueries.all_tags.getQuery().getQuery());
		List<FullTag> fullTagsList = new ArrayList<FullTag>();
		for (QueryResult.Record record : qr) {
			Scrambler scrambler = ScramblerFactory.get((String)record.get(TagTable.SCRAMBLER));
			FullTag fullTag = new FullTag(Long.parseLong(record.get("id").toString()), scrambler.descramble(record.get(TagTable.TITLE).toString()),
					Integer.parseInt(record.get("records_count").toString()));

			fullTagsList.add(fullTag);
		}

		return fullTagsList;
	}

	/**
	 * Получить теги записи
	 */
	public List<FullTag> getRecordTags(Long recordId) throws OgaiException {
		String queryText = TagsQueries.record_tags.getQuery().getQuery();
		queryText =String.format(queryText, recordId);
		final QueryResult qr = DBSession.selectQuery(queryText);

		return new ArrayList<FullTag>() {{
			for (QueryResult.Record record : qr) {
				Scrambler scrambler = ScramblerFactory.get((String)record.get(TagTable.SCRAMBLER));
				add(new FullTag(Long.parseLong(record.get("id").toString()), scrambler.descramble(record.get("title").toString()), null));
			}
		}};
	}

	/**
	 * Обновить тэги записи
	 */
	public void updateRecordTags(Long recordId, Set<String> recordNewTags, String scramblerName) throws OgaiException {
		RecordTagsUpdater.run(recordId, recordNewTags, scramblerName);
	}
}
