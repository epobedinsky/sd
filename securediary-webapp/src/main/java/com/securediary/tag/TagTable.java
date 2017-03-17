package com.securediary.tag;

import org.ogai.model.table.Table;

/**
 * TODO Class Description
 *
 * @author Побединский Евгений
 *         12.06.14 18:21
 */
public class TagTable extends Table {
	public static final String TABLE_NAME = "tag$tag";
	public static final String SEQUENCE_NAME = "tag_id";

	//Имена столбцов
	public static final String TITLE = "title";
	public static final String SCRAMBLER = "scrambler";

	public TagTable() {
		super(TABLE_NAME, SEQUENCE_NAME);
	}

	@Override
	protected void define() {
		//
	}
}
