package com.securediary.command;

import com.securediary.calendar.RecordEntity;
import com.securediary.tag.FullTag;
import com.securediary.tag.TagsService;
import org.ogai.command.HTMLCommand;
import org.ogai.core.Ctx;
import org.ogai.core.ObjectsRegistry;
import org.ogai.core.ServicesRegistry;
import org.ogai.exception.OgaiException;
import org.ogai.model.CommandCall;
import org.ogai.model.GoCommandCall;
import org.ogai.model.SubmitCommandCall;
import org.ogai.view.NullView;
import org.ogai.view.View;
import org.ogai.view.elements.LabelElement;
import org.ogai.view.elements.LinkElement;
import org.ogai.view.html.renderers.HTMLLinkRenderer;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * TODO Class Description
 *
 * @author Побединский Евгений
 *         13.06.14 17:25
 */
public class TagsListCommand extends HTMLCommand {
	public static final String NAME = "tagslist";

	@Override
	protected View doExecute(View view) throws OgaiException {
		PrintWriter responseWriter = null;
		try {
			responseWriter = Ctx.get().getResponse().getWriter();

			TagsService tagsService = (TagsService)ServicesRegistry.getInstance().get(TagsService.NAME);

			StringBuilder sb = new StringBuilder();
			for (FullTag ft : tagsService.getAllTags()) {
				//TODO переделать на ссылки
				sb.append("<b>" + ft.getTitle() + "(" + ft.getCount() + ")</b>");
				sb.append("</br>");
			}

			sb.append("</br>");
			sb.append("</br>");
			LinkElement linkElement = new LinkElement(new SubmitCommandCall(ScrambleAllTagsCommand.NAME, SubmitCommandCall.Target.NEW), new LabelElement("Scramble"));
			HTMLLinkRenderer linkRenderer = (HTMLLinkRenderer) ObjectsRegistry.getInstance().getHTMLRenderer(LinkElement.NAME, sb);
			linkRenderer.render(linkElement);

			Ctx.get().getResponse().setContentType("text/html; charset=UTF-8");
			responseWriter.write(sb.toString());
			responseWriter.flush();
		} catch (IOException e) {
			throw new OgaiException(e);
		}

		return new NullView();
	}
}
