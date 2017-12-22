package org.ogai.db.types;

import org.ogai.db.DBSession;
import org.ogai.db.id.IdGenerator;
import org.ogai.exception.OgaiException;
import org.ogai.model.table.Table;

/**
 * Created by epobedinsky on 21.04.2017.
 */
public class MySQLIdGenerator implements IdGenerator {

    @Override
    public String getGenerationIdSentence(Table table) {
        return null;
    }

    @Override
    public Object getCurrentId(Table table, DBSession dbSession) throws OgaiException {
        return dbSession.getValue("SELECT MAX(" + table.getIdColumnName() + ") FROM " + table.getTableName());
    }
}
