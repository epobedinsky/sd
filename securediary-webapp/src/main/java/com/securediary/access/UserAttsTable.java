package com.securediary.access;

import org.ogai.model.table.Table;

/**
 * Таблица аттрибутов пользователя
 *
 * @author Побединский Евгений
 *         01.06.14 23:28
 */
public class UserAttsTable extends Table {
	public static final String TABLE_NAME = "us$useratts";
	public static final String NICKNAME_COLUMN = "nickname";

	public UserAttsTable() {
		super(TABLE_NAME, "");
	}

	@Override
	protected void define() {
		//
	}
}
