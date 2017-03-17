package org.ogai.model.entity;

import org.ogai.command.sys.GoCommand;
import org.ogai.command.sys.SubmitSaveCommand;
import org.ogai.core.ObjectsRegistry;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.model.EntityException;
import org.ogai.model.entity.builders.ViewBooleanConvertor;
import org.ogai.model.entity.builders.ViewEntityConvertor;
import org.ogai.model.entity.builders.ViewLongConvertor;
import org.ogai.model.table.Table;
import org.ogai.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сущность - прослойка между уровнем хранения данных и представления.
 * Преобразует данные для использования в отображении, а так же перенаправляет все операции по изменению, созданию или
 * удалению сущности в объект таблицы (Table).
 *
 * Есди нужна какая-то дополнительная логика, перекрывать методы.
 * Идиентифицируется именем name, задаваемым конструктором. Хранится по этому имени в ObjectsRegistry.
 * Для работы со всеми экземлярами этой сущности во всех потоках используется по одному этому объекту,
 * поэтому он должен быть stateless
 *
 * В наследниках нужно перекрыть метод createTable() который создаст и зарегает
 * ссответсвующий этой сущности объект таблицы
 *
 * @author Побединский Евгений
 *         15.04.14 17:42
 */
public abstract class Entity {
	private final String name;
	private Table table;

	protected Map<String, EntityField> fields;

	/**
	 * Имя сущности. Связывает эту сущность со всеми другими связанными с ней объектами
	 * @param name не пустое
	 */
	public Entity(String name) {
		assert Util.isNotEmpty(name);

		this.name = name;
	}

	/**
	 * Зарегистрировать новый объект сущности
	 * Вызывать из потокобезопасного кода один раз для каждого name в объекте Entity
	 *
	 * Имя сущности. Связывает эту сущность со всеми другими связанными с ней объектами
	 */
	public void register() {
		ObjectsRegistry.getInstance().register(name, this);
		table = createTable();
		//Настроить поля сущности
		defineBaseEntity();
		defineEntity();
		if (table != null) {
			table.register();
		}
	}

	/**
	 * Определяет стандартные поля сущности.
	 * Чтоб добавить свои нужно переопределить этот метод
	 */
	protected void defineEntity() {
	}

	/**
	 * Добавялет системные поля сущности - id и тип экземпляра сущности
	 */
	private void defineBaseEntity() {
		fields = new HashMap<String, EntityField>();

		//id
		fields.put(table.getIdColumnName(), new EntityField(new ViewLongConvertor(), false));

		//isNew
		fields.put(GoCommand.PARAM_IS_NEW, new EntityField(new ViewBooleanConvertor(), true));

		//target
		fields.put("target", new EntityField(true));

		//entity_name
		fields.put(SubmitSaveCommand.PARAM_ENTITY_NAME, new EntityField(true));

		//entity_name
		fields.put("cmdname", new EntityField(true));
	}

	/**
	 * @return Объект таблицы который будет использоваться для изменений экземпляра этой сущности
	 * в хранилище данных.
	 * Здесь может создаваться и регестрироваться несколько таблиц для одного Entity
	 * либо ни одной - возвращать null
	 */
	protected abstract Table createTable();

	/**
	 *
	 * @return Ненулевой список имен всех полей, крмое id
	 */
	protected abstract List<String> getAllFieldNames();

	/**
	 * Получить пустую сущность
	 */
	public QueryResult getEmpty() throws EntityException {
		List<String> fieldNames = getAllFieldNames();
		assert fieldNames != null;

		final QueryResult.Record record = new QueryResult.Record();
		for (String fieldName : fieldNames) {
			record.put(fieldName,  "");
		}

		//Добавляем id
		record.put(table.getIdColumnName(),  "");

		return new QueryResult() {{
			add(record);
		}};
	}

	/**
	 * Загрузить по id информацию по сущности
	 * @param id
	 * @return
	 */
	public QueryResult load(String id) throws EntityException {
		assert Util.isNotEmpty(id);

		try {
			return table.load(id); //exception
		} catch (Exception e) {
			EntityException ee = new EntityException(e);
			ee.setOperation(EntityException.Operation.LOAD);
			throw ee;
		}
	}

	/**
	 *
	 * @param viewRecord данные из View которые необходимо сохранить в системе
	 * @return id сохраненной записи
	 */
	public Long save(Map<String, String> viewRecord) throws OgaiException {
		assert viewRecord != null;
		assert viewRecord.containsKey(GoCommand.PARAM_ID);
		assert viewRecord.containsKey(GoCommand.PARAM_IS_NEW);

		//Преобразуем в record (содержит java - объекты, а не строки)
		QueryResult.Record record = getEntityData(viewRecord);  //exception
		//Если экземпляр новый
		if ((Boolean)record.get(GoCommand.PARAM_IS_NEW)) {
		   return insert(onlyNotHiddenFields(record)); //exception
		} else {
			Long id = (Long)record.get(GoCommand.PARAM_ID);
			update(id, onlyNotHiddenFields(record)); //exception
			return id;
		}
	}

	public Long insert(QueryResult.Record record) throws EntityException {
		assert record != null;

		try {
			return table.insert(record); //exception
		} catch (OgaiException e) {
			EntityException ee = new EntityException(e);
			ee.setOperation(EntityException.Operation.INSERT);
			throw ee;
		}
	}

	/**
	 * Внести изменения в запись по id объекта
	 * @param record именения
	 */
	public void update(Long id, QueryResult.Record record) throws EntityException {
		assert record != null;

		try {
			table.update(id, record); //exception
		} catch (Exception e) {
			EntityException ee = new EntityException(e);
			ee.setOperation(EntityException.Operation.UPDATE);
			throw ee;
		}
	}

	/**
	 * Удалить по id объекта
	 * @param id не null
	 */
	public void delete(Long id) throws EntityException {
		assert id != null;

		try {
			table.delete(id); //exception
		} catch (Exception e) {
			EntityException ee = new EntityException(e);
			ee.setOperation(EntityException.Operation.DELETE);
			throw ee;
		}
	}

	/**
	 * Для использования в наследниках
	 * @return Объект управления хранением объекта
	 */
	protected Table getTable() {
		return table;
	}

	protected QueryResult.Record getEntityData(Map<String, String> viewRecord) throws OgaiException {
		QueryResult.Record record = new QueryResult.Record();
		for (String column : viewRecord.keySet()) {
			if (fields.containsKey(column)) {
				ViewEntityConvertor convertor = fields.get(column).getViewEntityConvertor();
				record.put(column, convertor.toEntity(viewRecord.get(column)));
			} else {
				record.put(column, viewRecord.get(column));
			}
		}

		return record;
	}

	/**
	 *
	 * @param srcRecord Исходный record со всеми пришедшими из представления полями
	 * @return record без скрытых полей
	 */
	protected QueryResult.Record onlyNotHiddenFields(QueryResult.Record srcRecord) {
		QueryResult.Record record = new QueryResult.Record();
		for (String column : srcRecord.keySet()) {
			if (!fields.containsKey(column) || !fields.get(column).isHidden()) {
				record.put(column, srcRecord.get(column));
			}
		}

		return record;
	}
}
