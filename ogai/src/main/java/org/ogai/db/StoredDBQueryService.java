package org.ogai.db;

import org.ogai.db.types.DatabaseService;
import org.ogai.core.ServicesRegistry;

/**
 * Сервис получения текстов статических запросов
 * Чтоб в будущем можно было перейти на чтение запросов из файла или БД
 *
 * @author Побединский Евгений
 *         06.04.14 23:34
 */
public class StoredDBQueryService {
	public static final String NAME = "queries";

	public SQLQuery getQueryText(StoredDBQuery queryText) {
		DatabaseService dbService = (DatabaseService)ServicesRegistry.getInstance().get(DatabaseService.NAME);
		return dbService.getQuery().createInstance((String)queryText.getText());
	}
}
