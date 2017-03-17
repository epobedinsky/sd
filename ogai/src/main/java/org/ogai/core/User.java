package org.ogai.core;

import org.ogai.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Current system user
 *
 * @author Побединский Евгений
 *         22.03.14 21:28
 */
public class User {
	private Long id;
	private String login;
	private String fullName;
  	private ArrayList<? extends UserRole> roles;

	/**
	 *
	 * @param id of user on our DAL
	 * @param login
	 * @param roles
	 */
	public User(Long id, String login, String fullName, ArrayList<? extends UserRole> roles) {
		this.id = id;
		this.login = login;
		this.roles = roles;
		this.fullName = fullName;
	}

	public Long getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public List<? extends UserRole> getRoles() {
		return roles;
	}

	public String getFullName() {
		if (Util.isEmpty(fullName)) {
			return login;
		}
		return fullName;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", login='" + login + '\'' +
				", roles=" + rolesToString() +
				'}';
	}

	private String rolesToString() {
		StringBuilder sb = new StringBuilder();
		if (roles != null) {
			sb.append("[");
			int nCounter = 0;
			for (UserRole role : roles) {
				sb.append(String.format("%s - %s", role.getId(), role.getDisplayName()));
				nCounter++;
				if (nCounter != roles.size()) {
					sb.append(", ");
				}
			}
			sb.append("]");
			return sb.toString();
		}

		return "null";
	}
}
