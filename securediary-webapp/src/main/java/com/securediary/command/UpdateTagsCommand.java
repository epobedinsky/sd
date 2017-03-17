package com.securediary.command;

import com.securediary.tag.TagsService;
import org.ogai.command.HTMLCommand;
import org.ogai.command.sys.GoCommand;
import org.ogai.core.Ctx;
import org.ogai.core.ServicesRegistry;
import org.ogai.exception.OgaiException;
import org.ogai.util.Util;
import org.ogai.view.View;
import org.ogai.view.elements.LabelElement;

import java.util.HashSet;
import java.util.Set;

/**
 * Обновить записи комманды
 *
 * @author Побединский Евгений
 *         12.06.14 17:57
 */
public class UpdateTagsCommand extends HTMLCommand {
	public static final String NAME = "upd_tags";

	public static final String PARAM_TAGS = "tags";
	public static final String SCRAMBLER_NAME = "scrambler";

	@Override
	protected View doExecute(View view) throws OgaiException {
		//Получаем и проверяем параметры
		if (!Ctx.get().getRequestParams().containsKey(GoCommand.PARAM_ID)) {
			throw new OgaiException("record id is not set");
		}
		if (!Ctx.get().getRequestParams().containsKey(PARAM_TAGS)) {
			throw new OgaiException("tags is not set");
		}

		Long recordId = Long.parseLong(Ctx.get().getRequestParams().get(GoCommand.PARAM_ID));
		String tags = Ctx.get().getRequestParams().get(PARAM_TAGS);
		tags = tags.replaceAll(TagsService.TAGS_DELIMITER + " ", TagsService.TAGS_DELIMITER);
		final String[] tagsArray = tags.split(TagsService.TAGS_DELIMITER);

		Set<String> tagsSet = new HashSet<String>() {{
			for (String tag : tagsArray) {
				tag = tag.trim();
				if (Util.isNotEmpty(tag)) {
					add(tag);
				}
			}
		}};

		((TagsService)ServicesRegistry.getInstance().get(TagsService.NAME)).updateRecordTags(recordId, tagsSet, Ctx.get().getRequestParams().get(SCRAMBLER_NAME));
		view.add(new LabelElement("OK"));
		return view;
	}
}
