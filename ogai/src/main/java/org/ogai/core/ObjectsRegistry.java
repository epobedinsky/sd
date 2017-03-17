package org.ogai.core;

import org.ogai.exception.OgaiException;
import org.ogai.grid.GridProxyFactory;
import org.ogai.model.entity.Entity;
import org.ogai.model.entity.EntityModel;
import org.ogai.model.table.Table;
import org.ogai.util.Util;
import org.ogai.view.html.renderers.HTMLRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Реест всевозможных объектов системы
 *
 * @author Побединский Евгений
 *         06.04.14 20:50
 */
public class ObjectsRegistry {
	private static ObjectsRegistry instance;

	//Фабрики проксей гридов
	private Map<String, GridProxyFactory> gridProxyFactories;

	//Классы рендереров HTML
	//Имя элемента - рендерер в HTML
	private Map<String, Class<? extends HTMLRenderer>> htmlRenderers;

	//Объекты Entity. Ключ - имя этой сущности
	private Map<String, Entity> entities;

	//Таблицы. tableName - таблица
	private Map<String, Table> tables;

	//Модели отображения сущностей
	private Map<String, EntityModel> entityModels;

	/**
	 *
	 * @return Новый реестр объектов
	 * ВНИМАНИЕ! Этот метод должен быть вызван единажды из потокобезопасного кода
	 */
	public static  ObjectsRegistry createInstance() {
		if (instance != null) {
			throw new IllegalStateException("ObjectsRegistry.createInstance should be called only once!!!");
		}
		instance = new ObjectsRegistry();
		return instance;
	}

	/**
	 *
	 * @return Существующий реестр. Вызывать из любой точки кода, когда регистр создан и заполнен
	 */
	public static ObjectsRegistry getInstance() {
		if (instance == null) {
			throw new IllegalStateException("ObjectsRegistry instance should be initialized at this point!!");
		}
		return instance;
	}

	/**
	 * Зарегестировать новую фабрику проксей гридов.
	 * Вызывать только из потокобезопасного кода, один раз для каждого name
	 * @param name не пустое
	 * @param newGridProxyFactory Не null
	 */
	public void registerGridFactory(String name, GridProxyFactory newGridProxyFactory) {
		if (Util.isEmpty(name)){
			throw new IllegalArgumentException("ObjectsRegistry.registerGridFactory:Name can't be empty");
		}
		if (null == newGridProxyFactory){
			throw new IllegalArgumentException("ObjectsRegistry.registerGridFactory:Can't register null object");
		}
		if (gridProxyFactories.containsKey(name)){
			throw new IllegalArgumentException("ObjectsRegistry.registerGridFactory:Object with such name was already registred");
		}

		gridProxyFactories.put(name, newGridProxyFactory);
	}

	/**
	 * Зарегестировать класс нового ренедрера в HTML
	 * Вызывать только из потокобезопасного кода, один раз для каждого name
	 * @param name не пустое
	 * @param clazz Не null
	 */
	public void register(String name, Class<? extends HTMLRenderer> clazz) {
		if (Util.isEmpty(name)){
			throw new IllegalArgumentException("ObjectsRegistry.register HTML renderer:Name can't be empty");
		}
		if (null == clazz){
			throw new IllegalArgumentException("ObjectsRegistry.register HTML renderer:Can't register null object");
		}
		if (htmlRenderers.containsKey(name)){
			throw new IllegalArgumentException("ObjectsRegistry.register HTML renderer:Object with such name was already registred");
		}

		htmlRenderers.put(name, clazz);
	}

	/**
	 * Зарегестрировать новый Entity
	 * Вызывать только из потокобезопасного кода, один раз для каждого name
	 * @param name не пустое
	 * @param entity Не null
	 */
	public void register(String name, Entity entity) {
		if (Util.isEmpty(name)){
			throw new IllegalArgumentException("ObjectsRegistry.register entity:Name can't be empty");
		}
		if (null == entity){
			throw new IllegalArgumentException("ObjectsRegistry.register entity:Can't register null object");
		}
		if (entities.containsKey(name)){
			throw new IllegalArgumentException("ObjectsRegistry.register entity:Object with such name was already registred");
		}

		entities.put(name, entity);
	}

	/**
	 * Зарегестрировать новый Table
	 * Вызывать только из потокобезопасного кода, один раз для каждого tableName
	 * @param table Не null
	 */
	public void register(Table table) {
		if (null == table) {
			throw new IllegalArgumentException("ObjectsRegistry.register entity table:Can't register null object");
		}
		if (tables.containsKey(table.getTableName())){
			throw new IllegalArgumentException("ObjectsRegistry.register entity table:Object with such name was already registred");
		}

		tables.put(table.getTableName(), table);
	}

	/**
	 * Зарегестрировать новую модель отображения сущности
	 * Вызывать только из потокобезопасного кода, один раз для каждого name
	 * @param entityModel Не null
	 */
	public void register(EntityModel entityModel) {
		if (null == entityModel) {
			throw new IllegalArgumentException("ObjectsRegistry.register entity entity model:Can't register null object");
		}
		if (entityModels.containsKey(entityModel.getName())){
			throw new IllegalArgumentException("ObjectsRegistry.register entity model:Object with such name was already registred");
		}

		entityModels.put(entityModel.getName(), entityModel);
	}

	/**
	 *
	 * @param name
	 * @return Объект сущности
	 */
	public Entity getEntity(String name)  {
		assert entities.containsKey(name) : "Registry doesn't contain entity with such name";

		return entities.get(name);
	}

	/**
	 *
	 * @param name
	 * @return Фабрика проксей гридов, которая была зарегестирована под таким именем
	 */
	public GridProxyFactory getGridProxyFactory(String name)  {
		assert gridProxyFactories.containsKey(name) : "Grid proxies factory doesn't contain factory with such name";

		return gridProxyFactories.get(name);
	}

	/**
	 *
	 * @param name
	 * @return Рендерер в HTML зареганый с таким именем
	 */
	public HTMLRenderer getHTMLRenderer(String name, StringBuilder sb) throws OgaiException {
		assert htmlRenderers.containsKey(name) : "Grid proxies factory doesn't contain factory with such name";

		try {
			return htmlRenderers.get(name).getConstructor(StringBuilder.class).newInstance(sb);
		} catch (Exception e) {
			throw new OgaiException("HTML render isntantication error", e);
		}
	}

	/**
	 *
	 * @param name
	 * @return Объект таблицы
	 */
	public Table getTable(String name)  {
		assert tables.containsKey(name) : "Registry doesn't contain table with such name";

		return tables.get(name);
	}

	/**
	 *
	 * @param name
	 * @return Модель отображения сущности
	 */
	public EntityModel getEntityModel(String name)  {
		assert entityModels.containsKey(name) : "Registry doesn't contain entity model with such name";

		return entityModels.get(name);
	}

	private ObjectsRegistry() {
		gridProxyFactories = new HashMap<String, GridProxyFactory>();
		htmlRenderers = new HashMap<String, Class<? extends HTMLRenderer>>();
		entities = new HashMap<String, Entity>();
		tables = new HashMap<String, Table>();
		entityModels = new HashMap<String, EntityModel>();
	}

}
