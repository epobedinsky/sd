package org.ogai.test;

import org.ogai.command.Executable;
import org.ogai.core.Application;
import org.ogai.core.Ctx;
import org.ogai.db.DBSession;
import org.ogai.db.QueryResult;
import org.ogai.exception.OgaiException;
import org.ogai.grid.GridDataElement;
import org.ogai.view.NullView;
import org.ogai.view.View;
import org.ogai.view.json.JSONView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Тесты для системы
 *
 * @author Побединский Евгений
 *         30.03.14 23:08
 */
public class TestCommand implements Executable {
	public static final String NAME = "test";

	@Override
	public View execute() throws OgaiException {
		//TODO передавать в параметрах интересующие тесты

		return new NullView();
	}

//		JSONView view = new JSONView();
//		GridDataElement gde = new GridDataElement(1, 2);
//		gde.setRows(new QueryResult() {{
//			add(new Record(){{
//				put("id", "СЛ");
//				put("name", "<a href='www.google.com'>Шри-Ланка</a>");
//				put("iso", "СЛ");
//				put("printable_name", "<img src='../css/images/first.gif'>");
//			}});
//			add(new Record(){{
//				put("id", "СЛ2");
//				put("name", "<a href='www.google.com'>Шри-Ланка2</a>");
//				put("iso", "СЛ2");
//			}});
//		}});
//		view.add(gde);
//		view.render();
//		int d = 2 + 1;


//		try {
//		Ctx.get().getResponse().setContentType("text/html");
//		Ctx.get().getResponse().setCharacterEncoding("UTF-8");
//
//
//		//Работаем  напрямую c response
//		HttpServletResponse response = Ctx.get().getResponse();
//		assert response != null : "Response is null";
//
//		String testSql = "SELECT k.id, k.name, c.name as country_name\n" +
//				"FROM kurorts k \n" +
//				"INNER JOIN countries c ON c.id = k.id_country\n" +
//				"WHERE k.aeroport='f' AND c.name LIKE 'Египет%'\n" +
//				"ORDER BY c.name, k.name;";
//
//		String getCountryId = "SELECT currval('country_id')";
//
//		String insertCountrySql = "INSERT INTO countries(\n" +
//				"            id, name)\n" +
//				"    VALUES (nextval('country_id'), 'Египет2');";
//
//		String insertKurortSql = "INSERT INTO kurorts(\n" +
//				"            id, id_country, name, aeroport)\n" +
//				"    VALUES (nextval('kurort_id'), %d, 'Шарм-Эш_Шейх2', 'f');";
//
//		String getKurortId = "SELECT currval('kurort_id')";
//
//		String deleteCountry = "DELETE FROM countries WHERE id = ";
//		String deleteKurort = "DELETE FROM kurorts WHERE id = ";
//
//		Long newCountryId = null;
//		Long newKurortId = null;
//		QueryResult qr = null;
//
//		DBSession dbSession = DBSession.get();
//
//		//Тест 1 Инсерт в транзакции c откатом
//		try {
//			dbSession.open();
//
//			qr = dbSession.select(testSql);
//			echo("1.1 До добавления внутри транзакции", qr);
//
//			//Добавляем страну
//			dbSession.execute(insertCountrySql);
//			newCountryId = (Long)dbSession.getSequenceCurrentValue("country_id");
//
//			//Курорт
//			String actualSQL = String.format(insertKurortSql, newCountryId);
//			DBSession.executeQuery(actualSQL);
//
//			newKurortId = (Long)dbSession.getSequenceCurrentValue("kurort_id");
//
//			//SELECT
//			qr = DBSession.selectQuery(testSql);
//			echo("1.2 После добавления внутри транзакции", qr);
//
//			//откатить
//			dbSession.markForRollback();
//
//		} catch (Exception e) {
//			dbSession.markForRollback();
//			Application.log.error("e:", e);
//			throw e;
//		} finally {
//			dbSession.close();
//		}
//
//		//SELECT
//		qr = DBSession.selectQuery(testSql);
//		echo("1.3 После отката транзакции", qr);
//
//		//Тест 2 Транзация завершившая нормально
//		dbSession = DBSession.get();
//
//		//Тест 2 Инсерт в транзакции c коммитом
//		try {
//			dbSession.open();
//
//			//Добавляем страну
//			dbSession.execute(insertCountrySql);
//			newCountryId = (Long)dbSession.getSequenceCurrentValue("country_id");
//
//			//Курорт
////			String actualSQL = String.format(insertKurortSql, newCountryId);
////			DBSession.executeQuery(actualSQL);
//
//		} catch (Exception e) {
//			dbSession.markForRollback();
//			Application.log.error("e:", e);
//			throw e;
//		} finally {
//			dbSession.close();
//		}
//
//		qr = DBSession.selectQuery(testSql);
//		echo("2 После добавления после коммита транзакции", qr);
//
//		DBSession.executeQuery(deleteCountry + newCountryId.toString());
//		DBSession.executeQuery(deleteKurort + newKurortId.toString());
//		qr = DBSession.selectQuery(testSql);
//		echo("3 После удаления", qr);
//		} catch(Exception e){
//			Application.log.error("Error on echo test command " + e);
//			throw new OgaiException(e);
//		}
//		return new NullView();
//
//	}

	private void echo(String header, QueryResult qr) throws IOException {
		PrintWriter pw = Ctx.get().getResponse().getWriter();
		pw.write("<H1>" + header + "</H1>");
		pw.write("<br/>");
		pw.write("<table border='1'>");
		for (QueryResult.Record record : qr) {
			pw.write("<tr>");
			for (java.lang.Object obj : record.values()) {
				pw.write("<td>" + obj.toString() +"</td>");
			}
			pw.write("</tr>");
		}
		pw.write("</table>");
	}
}
