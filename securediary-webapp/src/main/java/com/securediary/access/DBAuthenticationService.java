package com.securediary.access;

import org.ogai.core.AuthenticationService;
import org.ogai.core.ObjectsRegistry;
import org.ogai.core.User;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.model.table.Table;

import java.util.ArrayList;

/**
 * All logic linked with authentication
 *
 * @author Побединский Евгений
 *         23.03.14 19:41
 */
public class DBAuthenticationService extends AuthenticationService {
	private User mainUser;
	private String mainUserPassword;

	public DBAuthenticationService() {
		//Main system user
		mainUser = new User(-1L, "epobedinsky-master", "Побединский Евгений", new ArrayList<SDUserRole>() {{
			add(SDUserRole.creator); //God role
		}});
		mainUserPassword = "ogai";
	}

	/**
	 *
	 * @param login
	 * @param password
	 * @return User if found with login and password. Null if not found
	 */
	@Override
	public User loadUser(final String login, final String password) throws OgaiException {
		//check if this is main user
		if (login.equals(mainUser.getLogin()) && password.equals(mainUserPassword)) {
			return mainUser;
		}

		//Пытаемся загрузить такого пользователя из базы
		Table usersTable = ObjectsRegistry.getInstance().getTable(UsersTable.TABLE_NAME);
		QueryResult result = usersTable.list(new QueryResult.Record() {{
			put(UsersTable.LOGIN, login);
			put(UsersTable.PASSWORD, password);
		}}); //exception

		User user = null;
		if (result.size() > 1) {
			throw new OgaiException("More then one user found for login & password specified");
		} else if (result.size() == 1) {
			user = toUser(usersTable.getIdColumnName(), result.get(0)); //exception
		}

		return user;
	}

	private User toUser(String idColumnName, final QueryResult.Record record) throws OgaiException {
		//Получаем ник
		Table userAttsTable = ObjectsRegistry.getInstance().getTable(UserAttsTable.TABLE_NAME);
		QueryResult result = userAttsTable.load(record.get(userAttsTable.getIdColumnName()).toString()); //exception

		return new User((Long)record.get(idColumnName),
				(String)record.get(UsersTable.LOGIN),
				(String)result.get(0).get(UserAttsTable.NICKNAME_COLUMN),
				//Роли пока не храним в БД, пока считаем всех пользователей системы админами
				new ArrayList<SDUserRole>() {{
					//TODO Сделать преобразователи в рамках задачи c сохранением
					add(SDUserRole.admin);
				}});
	}
}
