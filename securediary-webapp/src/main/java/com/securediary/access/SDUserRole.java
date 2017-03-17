package com.securediary.access;

import org.ogai.core.UserRole;
import org.ogai.exception.OgaiException;
import org.ogai.text.Text;

/**
 * Роль пользователя
 *
 * @author Побединский Евгений
 *         06.04.14 16:36
 */
public enum SDUserRole implements UserRole {
	creator(-1, AccessText.creator),
	admin(0, AccessText.admin),
	;

	public static SDUserRole getById(Integer id) throws OgaiException {
		for (SDUserRole role : SDUserRole.values()) {
			if (role.getId().equals(id)) {
				return role;
			}
		}

		throw new OgaiException("Unsupported SDUserRole value:" + id);
	}

	private Integer id;
	private AccessText name;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public Text getDisplayName() {
		return name;
	}

	private SDUserRole(Integer id, AccessText name) {
		this.id = id;
		this.name = name;
	}
}
