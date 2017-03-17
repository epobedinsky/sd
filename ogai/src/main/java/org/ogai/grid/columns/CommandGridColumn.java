package org.ogai.grid.columns;

import org.ogai.core.ObjectsRegistry;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.model.CommandCall;
import org.ogai.view.elements.LabelElement;
import org.ogai.view.elements.LinkElement;
import org.ogai.view.html.renderers.HTMLRenderer;

import java.util.HashSet;
import java.util.Set;

/**
 * грид с ссылкой на некоторое действие
 * Его следует добавлять после всех столбцов с данными
 *
 * @author Побединский Евгений
 *         10.04.14 23:08
 */
public class CommandGridColumn extends GridColumn {
	private Set<String> srcArgColumnsSet;
	private CommandCall commandCall;

	/**
	 * @param name Не пустое имя столбца. Именно с таким именем значение из результатов запроса пойдет в грид
	 * @param srcArgColumns имена колонок из результатов запроса, значения из оторых будут передаваться в эту комманду
	 */
	public CommandGridColumn(String name, CommandCall call, String...srcArgColumns) {
		super(name);

		this.commandCall = call;

		//Множество столбцов учавствующих в вызове команды
		srcArgColumnsSet = new HashSet<String>();
		for (String srcArgColumn : srcArgColumns) {
			srcArgColumnsSet.add(srcArgColumn);
		}
	}

	/**
	 * Вставляет в запись srcRecord в столбец с именем column комманду и перает в нее все аргументы
	 * Текущее значение в column становится отображаемым названием команды
	 * @param srcRecord запись
	 */
	@Override
	public void process(QueryResult.Record srcRecord) throws OgaiException {
		//Берем исходное значение
		Object srcValue = srcRecord.get(getName());

		//Из записи передаем все аргументы в команду
		for (String recordColumn : srcRecord.keySet()) {
			if (srcArgColumnsSet.contains(recordColumn) && srcRecord.get(recordColumn) != null) {
				commandCall.addParam(recordColumn, String.valueOf(srcRecord.get(recordColumn)));
			}
		}

		//Заменяем исходное значение на строковое представление комманды
		srcRecord.put(getName(), getToString(srcValue)); //exception
	}

	protected CommandCall getCommandCall() {
		return commandCall;
	}

	private String getToString(Object srcValue) throws OgaiException {
		//Готовим строковое значение из srcValue
		String srcValueString = srcValue == null ? " " : srcValue.toString();
		//Создаем компонент
		LinkElement linkElement = new LinkElement(this.commandCall, new LabelElement(srcValueString));
		//Создаем рендерер
		StringBuilder renderResult = new StringBuilder();
		HTMLRenderer renderer = ObjectsRegistry.getInstance().getHTMLRenderer(LinkElement.NAME, renderResult); //exception
		//Рендерим
		renderer.render(linkElement); //exception
		//Возвращаем результат рендеринга
		return renderResult.toString();

	}
}
