package org.ogai.view.json;

import org.json.JSONException;
import org.json.JSONWriter;
import org.ogai.core.Ctx;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.grid.GridDataElement;
import org.ogai.log.Log;
import org.ogai.log.LogFactory;
import org.ogai.view.View;
import org.ogai.view.elements.Element;
import org.ogai.view.json.renderers.JSONGridDataRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Set;

/**
 * Возвращение данных с сервера в виде JSON
 *
 * @author Побединский Евгений
 *         07.04.14 22:50
 */
public class JSONView extends View {
	private static final Log log = LogFactory.create(JSONView.class);
	private static final String JSON_CONTENT_TYPE = "application/json";

	//Писатель JSON. На верхнем уровне все объекты пишутся в него.
	//Результат его работы (сам json) пишется в передаваемый в него писатель
	private JSONWriter jsonWriter;

	//Писатель в который пишет jsonWriter. Содержит в себе текст JSON который и пишется
	//в конце концов в response
	private StringWriter writer;

	public JSONView() {
		this.writer = new StringWriter();
		this.jsonWriter = new JSONWriter(writer);
	}

	@Override
	public String toString() {
		return writer.toString();
	}

	@Override
	public void add(Element element) throws OgaiException {
		assert element != null : "Element is null";

		log.debug("Add element: " + element);

		//Рендерим
		//Пока рендерим только данные для грида
		//В будущем заменить на фабрику рендереров
		if (GridDataElement.NAME.equals(element.getName())) {
			JSONGridDataRenderer renderer = new JSONGridDataRenderer(this);
			renderer.render((GridDataElement)element);
		}

	}

	@Override
	public void render() {
		log.debug("Start rendering JSONView");
		HttpServletResponse resp = Ctx.get().getResponse();
		prepareJson(resp);

		try {

			log.debug("Result JSON:" + writer.toString());

			resp.getWriter().write(writer.toString());
			resp.flushBuffer();
			log.debug("Finished rendering JSONView");
		} catch (IOException e) {
			log.error("Exception occured during JSONView rendering:" + e.getMessage(), e);
		}
	}

	//------------------------ Методы рендеринга JSON --------------------------------------

	/////////////////////////
	// Рендеринг объектов

	/**
	 * Начинает объект JSON - последовательность пар ключ-значение "{"key"="value", "key2"=2...}"
	 * В итоге будет "{"key"="value", "key2"=2...}"
	 *
	 * Каждую новую такую пару нужно добавлять вызовов addAttribute(key, object)
	 * И в конце вызывать endObject
	 */
	public JSONView startObject() throws OgaiException {
		try {
			jsonWriter.object();
		} catch (JSONException je) {
			throw new OgaiException("Error start JSON Object", je);
		}
		return this;
	}

	/**
	 * Начинает объект JSON - последовательность пар ключ-значение "{"key"="value", "key2"=2...}" с именем objectName
	 * В итоге будет " "objectName" : {"key"="value", "key2"=2...}"
	 *
	 * Каждую новую такую пару нужно добавлять вызовов addAttribute(key, object)
	 * И в конце вызывать endObject
	 */
	public JSONView startObject(String objectName) throws OgaiException {
		try {
			jsonWriter.key(objectName);
			jsonWriter.object();
		} catch (JSONException je) {
			throw new OgaiException("Error start JSON Object with key:" + objectName, je);
		}
		return this;
	}

	public JSONView addAttribute(String key, Object value) throws OgaiException {
		try {
			jsonWriter.key(key).value(value);
		} catch (JSONException je) {
			throw new OgaiException("Error add JSON attribute " + key + ":" + value, je);
		}
		return this;
	}

	public JSONView endObject() throws OgaiException {
		try {
			jsonWriter.endObject();
		} catch (JSONException je) {
			throw new OgaiException("Error end JSON Object", je);
		}
		return this;
	}

	/////////////////////////
	// Рендеринг массивов и результатов запросов

	/**
	 *
	 * @param name имя массива который создаем
	 * после вызова этого метода должны посоедовать несколько вызовов addObject - endObject, каждый из которых сформирует
	 *             по элементу массива.
	 * Если ни одного такого вызова не будет, массив будет пустым []
	 * Создание массива должно обязательно завершиться вызовом endArray
	 * @return
	 */
	public JSONView startArray(String name) throws OgaiException {
		try {
			jsonWriter.key(name).array();
		} catch (JSONException je) {
			throw new OgaiException("Error start array with name:" + name, je);
		}
		return this;
	}

	public JSONView endArray() throws OgaiException {
		try {
			jsonWriter.endArray();
		} catch (JSONException je) {
			throw new OgaiException("Error end array", je);
		}
		return this;
	}

	public JSONView addQueryRecord(String objectName, QueryResult.Record record, Set<String> excludedColumnNames) throws OgaiException {
		startObject(objectName); //exception

		for (String columnName : record.keySet()) {
			if (!excludedColumnNames.contains(columnName)) {
				addAttribute(columnName, record.get(columnName)); //exception
			}
		}

		return endObject(); //exception
	}

	//------------------------ Методы рендеринга JSON КОНЕЦ ---------------------------------
	private void prepareJson(HttpServletResponse resp) {
		assert resp != null : "response is null";

		resp.setContentType(JSON_CONTENT_TYPE);
		resp.setCharacterEncoding("UTF-8");
	}
}
