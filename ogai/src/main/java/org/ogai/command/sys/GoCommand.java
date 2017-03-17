package org.ogai.command.sys;

import org.ogai.command.HTMLCommand;
import org.ogai.core.Ctx;
import org.ogai.core.ObjectsRegistry;
import org.ogai.exception.OgaiException;
import org.ogai.model.entity.EntityModel;
import org.ogai.model.State;
import org.ogai.util.Util;
import org.ogai.view.View;


/**
 * Комманда перехода
 *
 * @author Побединский Евгений
 *         10.04.14 23:32
 */
public class GoCommand extends HTMLCommand {
	public static final String NAME = "go";

	public static final String PARAM_ID = "id";
	public static final String PARAM_NAME = "name";
	public static final String PARAM_IS_NEW = "is_new";

	@Override
	public View doExecute(View view) throws OgaiException {
		Ctx ctx = Ctx.get();
		String id = ctx.getRequestParams().get(PARAM_ID);
		String name = ctx.getRequestParams().get(PARAM_NAME);

		if (Util.isEmpty(name)) {
			throw new OgaiException("name is not set", "Для go.do обязателен параметр id в запросе"); //exception
		}

		EntityModel entityModel = ObjectsRegistry.getInstance().getEntityModel(name);
		if (entityModel == null) {
			throw new OgaiException("Not found entity model by name:" + name); //exception
		}

		State state = entityModel.createState(id, name); //exception
		return entityModel.getModelView(state);
	}
}
