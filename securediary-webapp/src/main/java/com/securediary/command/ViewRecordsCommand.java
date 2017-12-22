package com.securediary.command;

import com.securediary.calendar.RecordEntity;
import com.securediary.calendar.RecordsQueries;
import com.securediary.scramble.Scrambler;
import com.securediary.scramble.ScramblerFactory;
import com.securediary.tag.FullTag;
import com.securediary.tag.TagsService;
import org.ogai.command.Executable;
import org.ogai.command.sys.GoCommand;
import org.ogai.core.Ctx;
import org.ogai.core.ObjectsRegistry;
import org.ogai.core.ServicesRegistry;
import org.ogai.db.DBSession;
import org.ogai.db.QueryResult;
import org.ogai.db.SQLQuery;
import org.ogai.db.types.DatabaseService;
import org.ogai.exception.OgaiException;
import org.ogai.model.GoCommandCall;
import org.ogai.model.SubmitCommandCall;
import org.ogai.util.Util;
import org.ogai.view.NullView;
import org.ogai.view.View;
import org.ogai.view.elements.LabelElement;
import org.ogai.view.elements.LinkElement;
import org.ogai.view.html.renderers.HTMLLinkRenderer;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

/**
 * Отобразить комманды, id которых передали в параметрах
 *
 * @author Побединский Евгений
 *         07.06.14 17:39
 */
public class ViewRecordsCommand implements Executable {
	public static final String NAME = "viewrec";

	public static final String PARAM_IDS_LIST = "id";

	@Override
	public View execute() throws OgaiException {
		try {
			assert Ctx.get().getRequestParams().containsKey(PARAM_IDS_LIST);

			PrintWriter responseWriter = Ctx.get().getResponse().getWriter();

			//Загружаем записи
			QueryResult records = loadRecords(Ctx.get().getRequestParams().get(PARAM_IDS_LIST));

			//Каждую из них добавляем в вывод
			StringBuilder sb = new StringBuilder();
			for (QueryResult.Record record : records) {
				sb.append(render(record));
			}

			Ctx.get().getResponse().setContentType("text/html; charset=UTF-8");
            responseWriter.write(sb.toString());
			responseWriter.flush();
		} catch (Exception e) {
			throw new OgaiException(e);
		}

		return new NullView();
	}

	private QueryResult loadRecords(String idsListParam) throws OgaiException {
		String sql = RecordsQueries.view_records.getQuery().getQuery();
		DatabaseService dbService = (DatabaseService)ServicesRegistry.getInstance().get(DatabaseService.NAME);
		SQLQuery sqlQuery = dbService.getQuery();
		sqlQuery.setQuery(String.format(sql, idsListParam));
		return DBSession.selectQuery(sqlQuery);
	}

	private String render(QueryResult.Record record) throws OgaiException, ParseException {
		//Скрэмблер
		Scrambler scrambler = ScramblerFactory.get((String)record.get(RecordEntity.COL_SCRAMBLER));

		StringBuilder sb = new StringBuilder();
		sb.append("<H3>");
		sb.append(Util.toViewDateTime(String.valueOf(record.get(RecordEntity.COL_CREATE_DATE))) + " "
				+ scrambler.descramble((String)record.get(RecordEntity.COL_TITLE)));
		sb.append("</H3>");

		//Ссылка на изменение
		Long recordId = Long.parseLong(record.get(RecordEntity.COL_PAGE_REC_ID).toString());
		LinkElement linkElement = new LinkElement(new GoCommandCall(recordId.toString(), RecordEntity.NAME, SubmitCommandCall.Target.NEW), new LabelElement("Изменить"));
		HTMLLinkRenderer linkRenderer = (HTMLLinkRenderer)ObjectsRegistry.getInstance().getHTMLRenderer(LinkElement.NAME, sb);
		linkRenderer.render(linkElement);

		sb.append("<br>");
		//TODO пока работаем только с одной страницей в каждой записи
		//sb.append("<textarea width=\"100%\" height=\"100%\" readonly>");
		String descrambledText = scrambler.descramble((String)record.get(RecordEntity.getPageCol(RecordEntity.COL_PAGE_CONENT)));
		descrambledText = descrambledText.replaceAll("<FP>", "<br>");
		sb.append(descrambledText);
		//sb.append("</textarea>");
		sb.append("<br>");

		//TODO теги
		sb.append(renderTags(recordId, scrambler.getName()));

	    //TODO реализовать операции с тегами, удаление, редактирования
		sb.append("<br>");

		return sb.toString();
	}

	private String renderTags(Long recordId, String scramblerName) throws OgaiException {
		StringBuilder sb = new StringBuilder();

		//Форма
		sb.append("<form action=\"upd_tags.do\" method=\"POST\" name=\"tags_"+ recordId + "\"" +
				"novalidate=\"novalidate\" id=\"tags_"+ recordId + "\">");

		//Скрытое поле с id записи
		sb.append("<input type=\"hidden\"" +
				" name=\"" + GoCommand.PARAM_ID + "\" value=\"" + recordId + "\">");

		//Скрытое поле с скрамблером
		sb.append("<input type=\"hidden\"" +
				" name=\"" + UpdateTagsCommand.SCRAMBLER_NAME + "\" value=\"" + scramblerName + "\">");

		TagsService tagsService = (TagsService)ServicesRegistry.getInstance().get(TagsService.NAME);

		//Значение поля ввода - список тегов через запятую
		List<FullTag> tagsList = tagsService.getRecordTags(recordId);
		StringBuilder valueSb = new StringBuilder();
		boolean isFirst = true;
		for (FullTag fullTag : tagsList) {
			if (!isFirst) {
				valueSb.append(TagsService.TAGS_DELIMITER);
				//Пробел для читаемости, потом уберем
				valueSb.append(" ");
			}
			valueSb.append(fullTag.getTitle());
			isFirst = false;
		}

		//Поле ввода
		sb.append("<input value=\"" + valueSb.toString() +"\" style=\"width:100%\" name=\""
				+ UpdateTagsCommand.PARAM_TAGS + "\" type=\"text\">");

		//Кнопка
		sb.append("<input type=\"submit\" id=\"btn-primary\" value=\"OK\">");

		sb.append("</form>");
		return sb.toString();
	}
}
