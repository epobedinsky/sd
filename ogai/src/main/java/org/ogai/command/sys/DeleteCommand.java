package org.ogai.command.sys;

import org.ogai.command.HTMLCommand;
import org.ogai.core.Ctx;
import org.ogai.core.ObjectsRegistry;
import org.ogai.exception.OgaiException;
import org.ogai.model.entity.Entity;
import org.ogai.util.Util;
import org.ogai.view.RedirectView;
import org.ogai.view.View;


/**
 * Удалить сущность с экрана сущности.
 * Работает как HTMLAction.
 * TODO протестировать
 * TODO Переделать под AJAX (#17)
 *
 * @author Побединский Евгений
 *         12.04.14 17:33
 */
public class DeleteCommand extends HTMLCommand {
	public static final String NAME = "delete";

	public static final String PARAM_ID = "id";
	public static final String PARAM_NAME = "name";

	@Override
	public View doExecute(View view) throws OgaiException {
		Ctx ctx = Ctx.get();
		String id = ctx.getRequestParams().get(PARAM_ID);
		String name = ctx.getRequestParams().get(PARAM_NAME);

		if (Util.isEmpty(id)) {
			throw new OgaiException("id is not set", "Для delete.do обязателен параметр id в запросе");
		}

		if (Util.isEmpty(name)) {
			throw new OgaiException("name is not set", "Для delete.do обязателен параметр id в запросе");
		}

		Entity entity = ObjectsRegistry.getInstance().getEntity(name);
		entity.delete(Long.parseLong(id));
		view = new RedirectView("sampleGrid.jsp");
		return view;
	}
}
