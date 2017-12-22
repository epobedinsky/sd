package org.ogai.db.types;

import org.ogai.db.SQLQuery;
import org.ogai.db.id.IdGenerator;

/**
 * Created by epobedinsky on 16.04.2017.
 */
public class MySQLDatabaseService implements DatabaseService {
    private static MySQLIdGenerator generator = null;

    @Override
    public synchronized IdGenerator getIdGenerator() {
        if (generator == null) {
            generator = new MySQLIdGenerator();
        }
        return generator;
    }

    @Override
    public SQLQuery getQuery() {
        return new MySQLQuery();
    }
}
