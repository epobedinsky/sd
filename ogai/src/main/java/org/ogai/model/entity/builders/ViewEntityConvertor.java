package org.ogai.model.entity.builders;

import org.ogai.exception.OgaiException;

/**
 * Интерфейс преобразований данных из View (строки) в java-объекты
 * которые могут использоваться в коде серверной логике и в методах Entity в частности
 *
 * T - тип java объекта
 *
 * @author Побединский Евгений
 *         17.04.14 23:53
 */
public interface ViewEntityConvertor<T> {

	/**
	 * Преобразовать значение для отображения в View
	 * @param entityValue java - объект
	 * @return Строковое значение для View
	 *
	 * TODO Пока не используем
	 */
	//String toView(T entityValue);

	/**
	 * Преобразовать значение для использования в Entity и бизнес-логике
	 * @param viewValue строковое значение из View
	 * @return
	 */
	T toEntity(String viewValue) throws OgaiException;
}
