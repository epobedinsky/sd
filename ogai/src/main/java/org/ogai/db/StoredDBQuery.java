package org.ogai.db;

import org.ogai.text.Text;

/**
 * Статический запрос хранящийся в системе некоторым образом
 *
 * @author Побединский Евгений
 *         06.04.14 23:32
 */
public interface StoredDBQuery extends Text {
	SQLQuery getQuery();
}
