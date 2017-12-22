package com.securediary.command;

import com.securediary.calendar.RecordEntity;
import com.securediary.scramble.Scrambler;
import com.securediary.scramble.ScramblerFactory;
import com.securediary.scramble.SimpleScrambler;
import org.ogai.command.Executable;
import org.ogai.command.sys.GoCommand;
import org.ogai.core.Ctx;
import org.ogai.core.ObjectsRegistry;
import org.ogai.core.ServicesRegistry;
import org.ogai.exception.OgaiException;
import org.ogai.util.Util;
import org.ogai.view.NullView;
import org.ogai.view.View;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Обработать новую запись (Перенести в систему из файла)
 *
 * @author Побединский Евгений
 *         02.06.14 22:45
 */
public class ProcessNewRecordCommand extends GoMainPageCommand {
	public static final String NAME = "newrec";

	public static final String SRC_FILE_PATH = "src/src.txt";

	//Все новые записи скрамблируем SimpleScrambler
	public static final String  DEFAULT_SCRAMBLER_NAME = SimpleScrambler.NAME;

	@Override
	public View execute() throws OgaiException {
		try {
			Long id = createNewRecord();

			Ctx.get().getResponse().setContentType("text/html; charset=UTF-8");

			if (id == null) {
				Ctx.get().getResponse().getWriter().write("Исходный файл пуст!");
				Ctx.get().getResponse().getWriter().flush();
				return new NullView();
			}

			//Вызываем действие показа записи
			//Ctx.get().getRequestParams().put(PARAM_IDS_LIST, id.toString());
			Ctx.get().getRequestParams().put(GoCommand.PARAM_ID, id.toString());
			super.execute();
		} catch (Exception e) {
			throw new OgaiException(e);
		}

		return new NullView();
	}

	/**
	 * Создаем новую запись, читаем в нее из текстового файла в папке
	 */
	private Long createNewRecord() throws IOException, OgaiException {
		//Зачитваем содержимое файла
		List<String> fileLines = readFile();

		if (fileLines.isEmpty()) {
			//нет записей, возвращаем null
			return null;
		}

		//шифруем выбранным шифрователем

		List<String> scrambledLines = scramble(fileLines);

		//Сохраняем в БД
		return save(scrambledLines);
	}

	private List<String> readFile() throws IOException {
		File file = new File(SRC_FILE_PATH);
		InputStream is = Files.newInputStream(file.toPath());
		InputStreamReader psr = new InputStreamReader(is, Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(psr);

		String nextLine;
		List<String> stringsList = new ArrayList<String>();
		while((nextLine = br.readLine()) != null) {
			stringsList.add(nextLine);
		}

		return stringsList;
	}

	private List<String> scramble(List<String> srcLines) {
		List<String> scrambled = new ArrayList<String>();

		Scrambler scrambler = ScramblerFactory.get(DEFAULT_SCRAMBLER_NAME);
		for (String srcLine : srcLines) {
			scrambled.add(scrambler.scramble(srcLine));
		}
		return scrambled;
	}

	private Long save(List<String> scrambledLines) throws OgaiException {
		Map<String, String> params = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		for (String nextLine : scrambledLines) {
			sb.append(nextLine);
			sb.append("<br>");
		}
		params.put(GoCommand.PARAM_ID, "-1");
		params.put(GoCommand.PARAM_IS_NEW, "1");

		params.put(RecordEntity.COL_PAGE_CONENT, sb.toString());
		params.put(RecordEntity.COL_SCRAMBLER, DEFAULT_SCRAMBLER_NAME);
		return ObjectsRegistry.getInstance().getEntity(RecordEntity.NAME).save(params);
	}
}
