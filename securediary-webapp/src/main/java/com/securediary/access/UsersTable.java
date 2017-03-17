package com.securediary.access;

import org.ogai.model.table.Table;

/**
 * Таблица с пользователями системы
 *
 * @author Побединский Евгений
 *         16.04.14 20:46
 */
public class UsersTable extends Table {
	public static final String TABLE_NAME = "us$user";
	public static final String SEQUENCE_NAME = "user_id";

	//Имена столбцов
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";

	public UsersTable() {
		super(TABLE_NAME, SEQUENCE_NAME);
	}

	@Override
	protected void define() {
		//Ничего не переопределяем
	}
}
