package org.ogai.model.entity.builders;

import org.ogai.util.Util;

/**
 * Преобразователь в Long
 *
 * @author Побединский Евгений
 *         17.04.14 23:59
 */
public class ViewLongConvertor implements ViewEntityConvertor<Long> {

	@Override
	public Long toEntity(String viewValue) {
		if (Util.isEmpty(viewValue)) {
			return null;
		}

		return Long.parseLong(viewValue);
	}
}
