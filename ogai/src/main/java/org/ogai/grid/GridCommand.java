package org.ogai.grid;

import org.ogai.command.Executable;
import org.ogai.core.Ctx;
import org.ogai.core.ObjectsRegistry;
import org.ogai.exception.OgaiException;
import org.ogai.util.Util;
import org.ogai.view.View;
import org.ogai.view.json.JSONView;

/**
 * Обработать изменение параметров грида
 *
 * @author Побединский Евгений
 *         06.04.14 17:38
 */
public class GridCommand implements Executable {
	public static final String NAME = "grid";

	public static final String GRIDNAME_PARAM = "gridname";

	@Override
	//TODO проработать обработку ошибок
	public View execute() throws OgaiException {
		JSONView view = new JSONView();

		String gridName = Ctx.get().getRequestParams().get(GRIDNAME_PARAM);
		if (Util.isNotEmpty(gridName)) {
			GridProxy gridProxy = ObjectsRegistry.getInstance().getGridProxyFactory(gridName).create();
			GridData gd = gridProxy.getData();

			//TODO сделать чтоб эти параметры брались из описания грида
			GridDataElement gridDataElement = new GridDataElement(1, 2);
			gridDataElement.setRows(gd.getQueryResult());

			view.add(gridDataElement);
		}

		return view;
	}
}
