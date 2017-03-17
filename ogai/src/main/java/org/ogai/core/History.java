package org.ogai.core;

import org.ogai.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Current position of user in system (with a few previous steps)
 *
 * @author Побединский Евгений
 *         22.03.14 22:50
 */
public class History implements Serializable {
	private static final int MAX_LENGTH = 7;

	/**
	 * One pah step
	 */
	public static class HistoryStep implements Serializable {
		private String id;
		private String name;
		private String displayString;

		/**
		 *
		 * @param id  сущности Может быть null
		 * @param name  сущности. Не может быть null
		 * @param displayString Отображаемое назыание для этой ссылки в бредкрамбсе. Пока не используется
		 */
		public HistoryStep(String id, String name, String displayString) {
			assert Util.isNotEmpty(name);

			this.id = id;
			this.name = name;
			this.displayString = displayString;
		}

		public String getDisplayString() {
			return displayString;
		}

		public void setDisplayString(String displayString) {
			this.displayString = displayString;
		}

		@Override
		public String toString() {
			return "HistoryStep{" +
					"id='" + id + '\'' +
					", name='" + name + '\'' +
					", displayString='" + displayString + '\'' +
					'}';
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	private List<HistoryStep> previousSteps;
	private HistoryStep now;

	public History() {
		previousSteps = new ArrayList<HistoryStep>();
	}

	public void newStep(String id, String name) {
		if (now != null) {
			previousSteps.add(now);
		}

		//TODO у нас пока нет displayName, добавить когда будет компонент бредкрамбс
		now = new HistoryStep(id, name, "");
	}

	/**
	 *
	 * @return Шаг с параметрами для перезагрузки текущего окна.
	 * null если ни на какое окно еще не заходили
	 */
	public HistoryStep reloadStep() {
		return now;
	}

	@Override
	public String toString() {
		return "History{" +
				"previousSteps=" + previousSteps +
				", now=" + now +
				'}';
	}
}
