package org.ogai.model.entity;

import org.ogai.exception.OgaiException;
import org.ogai.model.entity.builders.ViewEntityConvertor;

/**
 * Поле сущности.
 * Определяет правила преобразования из View в Java-объекты и обратно
 * (определяются конвертером (TODO заменить на builder))
 * и свойства этого поля для сущности
 *
 * @author Побединский Евгений
 *         17.04.14 23:51
 */
public class EntityField {
	private ViewEntityConvertor viewEntityConvertor;
	private boolean isHidden;

	/**
	 *
	 * @param viewEntityConvertor преобразователь значений
	 * @param hidden true - если это значение не следует менять в хранилище данных
	 */
	public EntityField(ViewEntityConvertor viewEntityConvertor, boolean hidden) {
		assert  viewEntityConvertor != null;

		this.viewEntityConvertor = viewEntityConvertor;
		isHidden = hidden;
	}

	/**
	 *
	 * @param hidden true - если это значение не следует менять в хранилище данных
	 */
	public EntityField(boolean hidden) {
		isHidden = hidden;

		//Пустой преобразователь который просто данные переносит
		this.viewEntityConvertor = new ViewEntityConvertor<String>() {
			@Override
			public String toEntity(String viewValue) throws OgaiException {
				return viewValue;
			}
		};
	}

	public ViewEntityConvertor getViewEntityConvertor() {
		return viewEntityConvertor;
	}


	public boolean isHidden() {
		return isHidden;
	}

}
