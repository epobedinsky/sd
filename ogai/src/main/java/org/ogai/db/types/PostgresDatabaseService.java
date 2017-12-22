package org.ogai.db.types;

import org.ogai.db.SQLQuery;
import org.ogai.db.id.IdGenerator;

/**
 * Created by epobedinsky on 23.03.2017.
 */
public class PostgresDatabaseService implements DatabaseService {
    @Override
    public IdGenerator getIdGenerator() {
        return null;
    }

    @Override
    public SQLQuery getQuery() {
        return new SQLQuery();
    }
}
