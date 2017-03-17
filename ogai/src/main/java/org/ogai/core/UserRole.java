package org.ogai.core;

import org.ogai.text.Text;

/**
 * Роль пользователя
 *
 * @author Побединский Евгений
 *         06.04.14 15:33
 */
public interface UserRole {
	public Integer getId();

	/**
	 *
	 * @return Название роли отображаемое в ui и логах
	 */
	public Text getDisplayName();
}
