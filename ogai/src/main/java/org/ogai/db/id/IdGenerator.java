package org.ogai.db.id;

import org.ogai.db.DBSession;
import org.ogai.exception.OgaiException;
import org.ogai.model.table.Table;

/**
 * Created by epobedinsky on 18.03.2017.
 */
public interface IdGenerator<T> {
    String getGenerationIdSentence(Table table);
    T getCurrentId(Table table, DBSession dbSession) throws OgaiException;
}
