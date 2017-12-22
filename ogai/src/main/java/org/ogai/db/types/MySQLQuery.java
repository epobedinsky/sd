package org.ogai.db.types;

import org.ogai.db.QueryResult;
import org.ogai.db.SQLQuery;

/**
 * Created by epobedinsky on 16.04.2017.
 */
public class MySQLQuery extends SQLQuery {
    public MySQLQuery() {
        super();
    }

    public MySQLQuery(String query) {
        super(query);
    }


    @Override
    protected String createColumnsList(QueryResult.Record record, String idColumnName) {
        StringBuilder sb = new StringBuilder();
        boolean isFirstParam = true;
        for (String column : record.keySet()) {
            if (!column.equals(idColumnName)) {
                String value = column;
                if (!isFirstParam) {
                    value = COMMA + value;
                } else {
                    isFirstParam = false;
                }
                sb.append(value);
            }
        }

        return sb.toString();
    }

    @Override
    protected String createValuesListForInsert(QueryResult.Record record, String idColumnName) {
        return super.createValuesList(record, idColumnName);
    }

    @Override
    public SQLQuery createInstance(String queryText) {
        return new MySQLQuery(queryText);
    }
}
