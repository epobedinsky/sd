package org.ogai.db.types;

import org.ogai.db.SQLQuery;
import org.ogai.db.id.IdGenerator;

/**
 * Created by epobedinsky on 23.03.2017.
 */
public interface DatabaseService {
    public static final String NAME = "db_service";

    IdGenerator getIdGenerator();

    SQLQuery getQuery();
}
