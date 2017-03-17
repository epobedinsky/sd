package com.securediary.tag;

import com.securediary.scramble.Scrambler;
import com.securediary.scramble.ScramblerFactory;
import org.ogai.command.sys.GoCommand;
import org.ogai.core.ObjectsRegistry;
import org.ogai.core.ServicesRegistry;
import org.ogai.db.DBSession;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;

import java.util.*;

/**
 * TODO Class Description
 *
 * @author Побединский Евгений
 *         12.06.14 18:49
 */
public class RecordTagsUpdater {
	//id записи
	private Long recordId;

	//Таблица с тегами
	private TagTable tagTable;

	//Сервис тегов
	private TagsService tagService;

	//Новые теги, которые нужно добавить
	private Set<FullTag> newTags;

	//Существующие теги, с которыми нужно связать запись
	private Map<Long, FullTag> inputTags;

	//Теги которые нужно отвязать
	private Set<Long> tagsToDelete;

	private Scrambler scrambler;

	public RecordTagsUpdater(Long recordId) {
		this.recordId = recordId;
	}

	public static void run(Long recordId, Set<String> recordNewTags, String scramblerName) throws OgaiException {
		RecordTagsUpdater updater = new RecordTagsUpdater(recordId);
		updater.init(recordNewTags, scramblerName);
		updater.run();
	}

	private void run() throws OgaiException {
		DBSession dbSession = DBSession.get();
		try {
			dbSession.open();

			//Добавляем новые теги
			addNewTags(dbSession);

			//Связываем с тегами
			String linkSQL = TagsQueries.create_record_tag_link.getQuery().getQuery();
			for (Long tagId : inputTags.keySet()) {
				dbSession.execute(String.format(linkSQL, tagId, recordId));
			}

			//Удаляем устаревние связи
			String delinkSQL = TagsQueries.delete_record_tag_link.getQuery().getQuery();
			for (Long tagId : tagsToDelete) {
				dbSession.execute(String.format(delinkSQL, tagId, recordId));
			}


		} catch (Exception e) {
			dbSession.markForRollback();
			throw new OgaiException(e);
		} finally {
			dbSession.close();
		}
	}

	private void addNewTags(DBSession dbSession) throws OgaiException {
		for (FullTag newTag : newTags) {
			Long newTagId = (tagTable.insert(toRecord(newTag), dbSession));
			inputTags.put(newTagId, new FullTag(newTagId, newTag.getTitle(), null));
		}
	}

	private QueryResult.Record toRecord(final FullTag tag) {
		return new QueryResult.Record() {{
			put(TagTable.TITLE, scrambler.scramble(tag.getTitle()));
			put(TagTable.SCRAMBLER, scrambler.getName());
			put(GoCommand.PARAM_ID, "-1");
		}};
	}

	private void init(Set<String> recordNewTags, String scramblerName) throws OgaiException {
		tagTable = (TagTable) ObjectsRegistry.getInstance().getTable(TagTable.TABLE_NAME);
		tagService = (TagsService) ServicesRegistry.getInstance().get(TagsService.NAME);
		tagsToDelete = new HashSet<Long>();
		scrambler = ScramblerFactory.get(scramblerName);

		//Получаем все существующие теги
		Map<String, FullTag> allExistingTags = getAllExistingTags();

		//Присланные теги, которые есть в системе помещаем в мап по id, которых нет - в Set
		initInputTags(allExistingTags, recordNewTags);

		//Получаем теги сейчас связанные с этой записью
		List<FullTag> existingRecordTags = tagService.getRecordTags(recordId);

		//Удаляем из присланных тегов, те, которые уже связаны с записью
		//и формируем список тегов, которые есть сейчас у записи, но отсутствуют в присланных - их нужно отвязать
		for (FullTag existingRecordTag : existingRecordTags) {
			if (inputTags.containsKey(existingRecordTag.getId())) {
				inputTags.remove(existingRecordTag.getId());
			} else {
				tagsToDelete.add(existingRecordTag.getId());
			}
		}
	}

	private Map<String, FullTag> getAllExistingTags() throws OgaiException {
		QueryResult allTags = tagTable.list(new QueryResult.Record());
		Map<String, FullTag> allExistingTags = new HashMap<String, FullTag>();
		for (QueryResult.Record tagRecord : allTags) {
			Scrambler scrambler = ScramblerFactory.get((String)tagRecord.get(TagTable.SCRAMBLER));
			allExistingTags.put(scrambler.descramble(tagRecord.get(TagTable.TITLE).toString()),
					new FullTag(Long.parseLong(tagRecord.get(tagTable.getIdColumnName()).toString()),
							tagRecord.get(tagTable.TITLE).toString(),null));
		}

		return allExistingTags;
	}

	private void initInputTags(Map<String, FullTag> allExistingTags, Set<String> recordNewTags) throws OgaiException {
		newTags = new HashSet<FullTag>();
		inputTags = new HashMap<Long, FullTag>();
		for (String recordNewTag : recordNewTags) {
			if (allExistingTags.containsKey(recordNewTag)) {
				Long id = allExistingTags.get(recordNewTag).getId();
				inputTags.put(id, new FullTag(id, recordNewTag, null));
			} else {
				newTags.add(new FullTag(null, recordNewTag, null));
			}
		}
	}
}
