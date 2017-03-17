package org.ogai.model.entity.builders;

import org.ogai.exception.OgaiException;
import org.ogai.util.Util;

/**
 *  @author Побединский Евгений
 *         18.04.14 0:21
 */
public class ViewBooleanConvertor implements ViewEntityConvertor<Boolean> {
	@Override
	public Boolean toEntity(String viewValue) throws OgaiException {
		if (Util.isEmpty(viewValue) || viewValue.equals("0")) {
			return Boolean.FALSE;
		} else if (Util.isNotEmpty(viewValue) && viewValue.equals("1")) {
			return Boolean.TRUE;
		} else if (Util.isNotEmpty(viewValue)) {
			throw new OgaiException("Unsuported boolean view value:" + viewValue);
		}

		assert false;
		return null;
	}
}
