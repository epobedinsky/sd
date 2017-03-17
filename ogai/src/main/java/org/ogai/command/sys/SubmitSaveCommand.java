package org.ogai.command.sys;

import org.ogai.command.HTMLCommand;
import org.ogai.core.Ctx;
import org.ogai.core.ObjectsRegistry;
import org.ogai.exception.OgaiException;
import org.ogai.view.View;

import java.util.Map;

/**
 * Сохранить сущность.
 * Вызывается POST - запросом
 * TODO написать такую же версию для AJAX (которая будет вызываться если экран открыт в новой вкладке,
 * эта версия будет вызываться если открыт в этой же вкладке )
 *
 * @author Побединский Евгений
 *         17.04.14 18:00
 */
public class SubmitSaveCommand extends HTMLCommand {
	public static final String NAME = "save";

	public static final String PARAM_ENTITY_NAME = "entity_name";

	@Override
	protected View doExecute(View view) throws OgaiException {
		Map<String, String> params = Ctx.get().getRequestParams();

		Long id = ObjectsRegistry.getInstance().getEntity(params.get(PARAM_ENTITY_NAME)).save(params); //exception

		//Перегружаем экран сущности
		//TODO Заменить на id сохраненного объекта
		return reloadCurrentWindow(id.toString());
	}
}
