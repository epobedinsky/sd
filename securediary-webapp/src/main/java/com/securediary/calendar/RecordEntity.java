package com.securediary.calendar;

import com.securediary.scramble.Scrambler;
import com.securediary.scramble.ScramblerFactory;
import org.ogai.command.sys.GoCommand;
import org.ogai.core.Application;
import org.ogai.core.Ctx;
import org.ogai.db.DBSession;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.model.EntityException;
import org.ogai.model.entity.Entity;
import org.ogai.model.entity.EntityField;
import org.ogai.model.entity.builders.ViewDateConvertor;
import org.ogai.model.entity.builders.ViewEntityConvertor;
import org.ogai.model.table.Table;
import org.ogai.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Сущность записи
 *
 * @author Побединский Евгений
 *         15.04.14 21:32
 */
public class RecordEntity extends Entity {
	public static final String NAME = "record";
	public static final String RECORD_TABLE_NAME = "rec$record";
	public static final String RECORD_PAGE_TABLE_NAME = "rec$recordpage";
	public static final String RECORD_SEQUENCE_NAME = "record_id";
	public static final String RECORD_PAGE_SEQUENCE_NAME = "recordpage_id";

	public static final String COL_CREATE_DATE = "create_date";
	public static final String COL_CREATOR_ID = "creator_id";
	public static final String COL_DIARY_ID = "diary_id";
	public static final String COL_SCRAMBLER = "scrambler";

	public static final String PAGE_PREFIX = "pagepfx_";
	public static final String COL_PAGE_REC_ID = "record_id";
	public static final String COL_PAGE_ORDERNUM = PAGE_PREFIX + "order_num";
	public static final String COL_PAGE_CONENT =  PAGE_PREFIX + "page_content";
	public static final String COL_TITLE = "title";

	public static String getPageCol(String column) {
		return column.replaceFirst(PAGE_PREFIX, "");
	}

	public RecordEntity() {
		super(NAME);
	}

	@Override
	protected void defineEntity() {
		super.fields.put("create_time", new EntityField(new ViewEntityConvertor() {
			@Override
			public Object toEntity(String viewValue) throws OgaiException {
				return viewValue;
			}
		}, true));
	}

	@Override
	protected QueryResult.Record getEntityData(Map<String, String> viewRecord) throws OgaiException {
		QueryResult.Record record = super.getEntityData(viewRecord);

		//дата добавления записи
		if (Boolean.parseBoolean(record.get(GoCommand.PARAM_IS_NEW).toString())) {
			record.put(COL_CREATE_DATE, new Date());

			//TODO пока считаем что страница всегда одна
			record.put(COL_PAGE_ORDERNUM, new Long(1));

			//TODO пока считаем, что все записи заносятся в дефолтный diary с id=0
			record.put(COL_DIARY_ID, 0);
			record.put(COL_CREATOR_ID, Ctx.get().getUser().getId());
		} else {
			ViewDateConvertor viewDateConvertor = new ViewDateConvertor();
			Date createDate = viewDateConvertor.toEntity(viewRecord.get(COL_CREATE_DATE));

			String time = viewRecord.get("create_time");
			if (Util.isNotEmpty(time)) {
				String[] timeParts = time.split(":");
				//Часы
				if (timeParts.length > 0) {
					createDate.setHours(Integer.parseInt(timeParts[0]));
				}

				//Минуты
				if (timeParts.length > 1) {
					createDate.setMinutes(Integer.parseInt(timeParts[1]));
				}

				//Секунды
				if (timeParts.length > 2) {
					createDate.setSeconds(Integer.parseInt(timeParts[2]));
				}
			}

			record.put(COL_CREATE_DATE, createDate);
			record.put(COL_TITLE, viewRecord.get(COL_TITLE));
			record.put(COL_SCRAMBLER, viewRecord.get(COL_SCRAMBLER));

			//Обработать изменение скрэмблера
			onScramblerChange((Long)(record.get(getTable().getIdColumnName()))
					, (String)record.get(COL_SCRAMBLER), record);
		}

		return record;
	}

	private void onScramblerChange(Long recordId, String newScramblerName, QueryResult.Record record) throws OgaiException {
		//находим старый скрэмблер
		QueryResult.Record oldRecord = getTable().load(recordId.toString()).get(0);
		String oldScramblerName = (String)oldRecord.get(COL_SCRAMBLER);
		Scrambler oldScrambler = ScramblerFactory.get(oldScramblerName);
		Scrambler newScrambler = ScramblerFactory.get(newScramblerName);
		//если старый скрэмбер отличен от нового
		if (!oldScrambler.equals(newScrambler) || Util.isEmpty((String)oldRecord.get(COL_TITLE))) {
			//Значение из тайтла перешифровываем новым скрамблером
			String scrambledTitle = newScrambler.scramble((String)record.get(COL_TITLE));
			record.put(COL_TITLE, scrambledTitle);


		}
	}

	@Override
	protected Table createTable() {
		return new Table(RECORD_TABLE_NAME, RECORD_SEQUENCE_NAME) {

			public static final String PAGE_TABLE_NAME = "rec$recordpage";

			@Override
			protected void define() {
				//Пока ничего не делаем
			}

			@Override
			protected String getSQLParam(String column, Object value) {
				//TODO #21
				if (column.equals(COL_CREATE_DATE)) {
					if (value == null) {
						return "null";
					}
					return "'" + Util.formatDate((Date)value,Util.DB_DATETIME_FORMAT_PATTERN ) + "'";
				} else {
					return super.getSQLParam(column, value);
				}
			}

			/**
			 * Вставить новую запись в таблицу
			 * @param record не null
			 *
			 * @throws org.ogai.exception.OgaiException
			 */
			@Override
			public Long insert(QueryResult.Record record) throws OgaiException {
				assert record != null;
				assert Util.isNotEmpty(this.sequenceName);

				DBSession dbSession = DBSession.get();

				QueryResult.Record recRecord = getRecord(record);

				//Добавляем запись и все ее страницы в одной транзакции
				try {
					dbSession.open();
					Long recordId = dbSession.executeInsertQueryInTrx(this.sequenceName, String.format(INSERT_SQL, tableName, //INSERT INTO tableName
							createColumnsList(recRecord), // (id, a1, b1...n)
							createValuesListForInsert(recRecord, idColumnName))); //VALUES(%s, a, b,...n) //exception

					insertPage(dbSession, recordId, getPage(record));

					return recordId;
				}catch (Exception e) {
					dbSession.markForRollback();
					throw new OgaiException(e);
				} finally {
					dbSession.close();
				}
			}


			private void insertPage(DBSession dbSession, Long recordId, QueryResult.Record page) throws OgaiException {
				page.put(COL_PAGE_REC_ID, recordId);
				page.put(idColumnName, "");

				dbSession.executeInsertQueryInTrx(RECORD_PAGE_SEQUENCE_NAME, String.format(INSERT_SQL, PAGE_TABLE_NAME, //INSERT INTO tableName
						createColumnsList(page), // (id, a1, b1...n)
						createValuesListForInsert(page, idColumnName))); //VALUES(%s, a, b,...n) //exception
			}

			private QueryResult.Record getRecord(QueryResult.Record src) {
				//Убераем все поля с префиксом страницы
				QueryResult.Record record = new QueryResult.Record();
				for (String key : src.keySet()) {
					if (!key.startsWith(PAGE_PREFIX)) {
						 record.put(key, src.get(key));
					}
				}
				return record;
			}

			public QueryResult.Record getPage(QueryResult.Record src) {
				//Убераем все поля без префикса страницы
				QueryResult.Record page = new QueryResult.Record();
				for (String key : src.keySet()) {
					if (key.startsWith(PAGE_PREFIX)) {
						page.put(key.replaceFirst(PAGE_PREFIX, ""), src.get(key));
					}
				}
				return page;
			}
		};

	}

	@Override
	protected List<String> getAllFieldNames() {
		//TODO
		return null;
	}
}
