package com.securediary.command;

import com.securediary.scramble.Scrambler;
import com.securediary.scramble.ScramblerFactory;
import com.securediary.tag.FullTag;
import com.securediary.tag.TagTable;
import com.securediary.tag.TagsService;
import org.ogai.command.HTMLCommand;
import org.ogai.command.sys.GoCommand;
import org.ogai.core.ObjectsRegistry;
import org.ogai.core.ServicesRegistry;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.view.View;
import org.ogai.view.elements.LabelElement;

import java.util.List;

/**
 * Заскрэмблировать ыче тэги
 *
 * @author Побединский Евгений
 *         06.07.14 19:49
 */
public class ScrambleAllTagsCommand extends HTMLCommand {
	public static final String NAME = "scramble_tags";

	@Override
	protected View doExecute(View view) throws OgaiException {
		List<FullTag> allTags = ((TagsService)ServicesRegistry.getInstance().get(TagsService.NAME)).getAllTags();
		TagTable tagTable = (TagTable) ObjectsRegistry.getInstance().getTable(TagTable.TABLE_NAME);
		for (FullTag tag : allTags) {
			tagTable.update(tag.getId(), toRecord(tag));
		}

		view.add(new LabelElement("OK"));
		return view;
	}

	private QueryResult.Record toRecord(final FullTag tag) {
		final Scrambler scrambler = ScramblerFactory.get(ProcessNewRecordCommand.DEFAULT_SCRAMBLER_NAME);
		return new QueryResult.Record() {{
			put(TagTable.TITLE, scrambler.scramble(tag.getTitle()));
			put(TagTable.SCRAMBLER, scrambler.getName());
			put(GoCommand.PARAM_ID, "-1");
		}};
	}
}
