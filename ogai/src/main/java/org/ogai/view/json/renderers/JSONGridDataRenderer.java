package org.ogai.view.json.renderers;

import org.json.JSONException;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.grid.GridDataElement;
import org.ogai.view.json.JSONView;

import java.util.HashSet;
import java.util.Set;

/**
 * рендерер для GridDataElement
 *
 * @author Побединский Евгений
 *         08.04.14 20:19
 */
public class JSONGridDataRenderer {
	public static final String ATT_NAME_PAGE = "page";
	public static final String ATT_NAME_TOTAL = "total";
	public static final String ATT_NAME_ROWS = "rows";
	public static final String ATT_NAME_CELL = "cell";

	public static final String COLUMNS_ID = "id";

	private static final Set<String> ROW_GEN_EXCLUDED_COLUMNS = new HashSet<String>() {{
		add(COLUMNS_ID);
	}};

	private JSONView view;

	public JSONGridDataRenderer(JSONView view) {
		this.view = view;
	}

	public void render(GridDataElement element) throws OgaiException {
		assert element != null : "Element in render is null";

		//Общие параметры грида
		view.startObject()
				.addAttribute(ATT_NAME_PAGE, element.getPageNumber())
				.addAttribute(ATT_NAME_TOTAL, element.getTotalPagesCount());

				//Строки
				view.startArray(ATT_NAME_ROWS);
				renderRows(view, element.getRows());
				view.endArray();

		//Конец всего объекта
		view.endObject();
	}

	/*
	[ { "cell" : { "iso" : "SL",
            "iso3" : "SLK",
            "name" : "<a href='www.google.com'>Шри-Ланка</a>",
            "numcode" : "09",
            "printable_name" : "<img src='../css/images/first.gif'>"
          },
        "id" : "СЛ"
      },
      { "cell" : { "iso" : "AE",
            "iso3" : "UAE",
            "name" : "Abu-Dabi",
            "numcode" : "7",
            "printable_name" : "United Arab Emirates"
          },
        "id" : "AE"
      }
    ]
	 */
	private void renderRows(JSONView view, QueryResult rows) throws OgaiException {
		assert rows != null;

		for (QueryResult.Record row : rows) {
			assert row.containsKey(COLUMNS_ID);

			view.startObject()
					.addAttribute(COLUMNS_ID, row.get(COLUMNS_ID))
					.addQueryRecord(ATT_NAME_CELL, row, ROW_GEN_EXCLUDED_COLUMNS)
			.endObject();
		}
	}
}
