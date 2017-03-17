package org.ogai.model.entity;

import org.ogai.command.sys.GoCommand;
import org.ogai.core.Ctx;
import org.ogai.core.ObjectsRegistry;
import org.ogai.db.QueryResult;
import org.ogai.model.EntityException;
import org.ogai.model.State;
import org.ogai.model.entity.Entity;
import org.ogai.view.RedirectView;
import org.ogai.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Объект экрана отображающего информацию по сущности
 * Каждый такой объект хранится в ObjectsRegistry по имени и обрабатываем все запросы
 * для отображения сущности с этим именем.
 *
 * Поэтому должен быть stateless. Состояние можно передавать по жизни объекта черех объект State
 *
 *
 * @author Побединский Евгений
 *         15.04.14 22:41
 */
public class EntityModel {
	private final String name;

	public EntityModel(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return Состояние модели
	 */
	public State createState(String id, String name) throws EntityException {
		State state = new State(id, name);
		initData(state); //exception

		//Добавляем этот шаг в историю
		Ctx.get().getHistory().newStep(id, name);

		return state;
	}

	/**
	 *
	 * @return Вид модели для рендеринга на клиент
	 */
	public View getModelView(State state) {
		//TODO implement normally
		//TODO преобразовать ViewResult в мап со строками (переписать на формирование элементов View)
		Map<String, String> rawValues = new HashMap<String, String>();
		for (QueryResult.Record record : state.getData()) {
			for (String columnName : record.keySet()) {
				rawValues.put(columnName, record.get(columnName) == null ? "" : String.valueOf(record.get(columnName)));
			}
		}
		//Добавляем информацию о том, новый это объект или нет
		//TODO Переделать на преобразование преобразователем
		rawValues.put(GoCommand.PARAM_IS_NEW, state.isInsert() ? "1" : "0");
		Ctx.get().getSession().setAttribute("entity", rawValues);
		return new RedirectView("entity.jsp");
	}

	/**
	 * Инициализировать данные для отображения моделью
	 */
	protected void initData(State state) throws EntityException {
		//Если модель открыта для создания нового экземпляра
		if (state.isInsert()) {
			state.setData(getEmpty());
		} else {
			//Открываем существующую - загружаем ее
		   	state.setData(loadEntity(state)); //exception
		}
	}

	/**
	 * Обработать состояние фрейма после загрузки.
	 * По умолчанию ничего не делает
	 * @param state
	 */
	protected void processData(State state) {
		//
	}

	//TODO написать метод получения кнопок

	/**
	 *
	 * @return Данные для новой записи этой сущности
	 */
	protected QueryResult getEmpty() throws EntityException {
		Entity entity = ObjectsRegistry.getInstance().getEntity(name);

		return entity.getEmpty(); //exception
	}

	protected QueryResult loadEntity(State state) throws EntityException {
		Entity entity = ObjectsRegistry.getInstance().getEntity(name);

		return entity.load(state.getId()); //exception
	}

	public String getName() {
		return name;
	}
}
