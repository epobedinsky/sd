<%@ page import="java.util.Map" %>
<%@ page import="org.ogai.command.sys.GoCommand" %>
<%@ page import="org.ogai.model.GoCommandCall" %>
<%@ page import="org.ogai.command.sys.SubmitSaveCommand" %>
<%@ page import="org.ogai.util.Util" %>
<%@ page import="com.securediary.calendar.RecordEntity" %>
<%@ page import="com.securediary.scramble.ScrambleUtil" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html lang="ru" class="ie ie6 lte9 lte8 lte7 os-win"> <![endif]-->
<!--[if IE 7]> <html lang="ru" class="ie ie7 lte9 lte8 lte7 os-win"> <![endif]-->
<!--[if IE 8]> <html lang="ru" class="ie ie8 lte9 lte8 os-win"> <![endif]-->
<!--[if IE 9]> <html lang="ru" class="ie ie9 lte9 os-win"> <![endif]-->
<!--[if gt IE 9]> <html lang="ru" class="os-win"> <![endif]-->
<!--[if !IE]><!-->
<html class="os-win" lang="ru"><!--<![endif]-->
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="application-name" content="Aquatour">
<link rel="stylesheet" type="text/css" href="css/login/css.css">
<link rel="stylesheet" type="text/css" href="css/login/css_004.css">
<title>Aquatour Webb</title>
<link rel="stylesheet" type="text/css" href="css/login/css_003.css">
<link rel="stylesheet" type="text/css" href="css/login/css_002.css">
<link rel="stylesheet" type="text/css" href="css/entity/aquatour.css">
<link href="smoothness/jquery-ui-1.9.2.custom.min.css" rel="stylesheet">
<script src="js/jquery-1.8.3.js"></script>
<script src="js/jquery-ui-1.9.2.custom.min.js"></script>
<style type="text/css" charset="utf-8">
</style>
<!-- Добавить сюда эксперементальные стили -->
</head>
<!-- Тнициализация datepicker -->
<script>
	function initDatePicker(datepickername) {
		$( "#" + datepickername ).datepicker({
			changeMonth:true, 
			changeYear:true, 
			constrainInput:true, 
			buttonImage:"img/calendar.png",
			firstDay:"1", 
			showOn:"button", 
			dateFormat:"<%= Util.JS_VIEW_DATE_FORMAT%>",
			dayNamesMin: ['Вс','Пн','Вт','Ср','Чт','Пт','Сб'],
			monthNamesShort: ['Янв', 'Фев', 'Март', 'Апр', 'Май','Июнь','Июль','Авг','Сен','Окт','Ноя','Дек']
			});
	}
		

	$(function() {
		initDatePicker("create_date");
	});
</script>
<%
    Map<String, String> data = (Map<String, String>)session.getAttribute("entity");
%>
<!-- id в body отвечает за серый цвет фона -->
<body dir="ltr" class="guest v2  chrome-v5 chrome-v5-responsive sticky-bg guest js" id="pagekey-uas-consumer-login-internal" > 
<!-- Черная полосочка по верху -->
<div id="header" class="global-header responsive-header nav-v5-2-header responsive-1 remote-nav" role="banner">
	<div id="top-header">
	<!-- Пункты на черной полосочке -->
	<ul class="nav main-nav guest-nav" role="navigation" style="float:left">
	   <li class="nav-item">
		<font class="nav-caption">
		  Запись
		</font>
	  </li>
	</ul>
	<!-- Пункты на черной полосочке КОНЕЦ -->
 
	</div>
</div>
<!-- Черная полосочка по верху КОНЕЦ -->

<!-- Отступы -->
<br>
<br>
<br>
<br>

<form action="save.do" method="POST" name="entity" novalidate="novalidate" id="entity_from">
<!-- Скрытые поля -->
<!-- id -->
<input type="hidden" name="<%= GoCommand.PARAM_ID %>" value="<%= data.containsKey("id") ? data.get("id") : "" %>">
<!-- name сущности -->
<input type="hidden" name="<%= SubmitSaveCommand.PARAM_ENTITY_NAME %>" value="<%= RecordEntity.NAME%>">
<!-- Отображаем новый экземпляр или существующий -->
<input type="hidden" name="<%=GoCommand.PARAM_IS_NEW%>" value="0">
<!-- тип открытия -->
<!-- Переделать чтою работало для разных target Баг #20 -->
<input type="hidden" name="target" value="<%= GoCommandCall.Target.NEW.toString() %>">
<!-- Внешняя таблица -->
<table class="collapsetable" cellpadding="0" cellspacing="0" border="0"><tbody>

	<!-- Строка с таблицей общей по всей ширины -->
	<tr><td colspan="3">
		<!-- Колонка полей - таблица  -->
		<table class="entitytable" cellpadding="100" cellspacing="0" border="0" style="width:70%"><tbody>
			<!-- Поля -->
			
			<!-- Заголовок -->
			<tr id="row_title">
				<!-- Отступ перед -->
				<td><div class="entityempty">&nbsp;</div></td>
				<!-- Заголовок поля -->
				<td class="entitycaption" nowrap="nowrap" valign="top" width="10%">Заголовок</td>
				<!-- Поле -->
				<td class="entitytext" align="left" valign="middle" width="100%">
					<input value="<%= ScrambleUtil.descramble(data.get("title"), data.get("scrambler"))%>" style="width:100%" maxlength="100" name="title" type="text">
				</td>
				<!-- Отступ после -->
				<td><div class="entityempty">&nbsp;</div></td>
			</tr>
			
			<!-- Строка - отступ между полями -->
			<tr>
				<td class="entityrows_gap"></td>
			</tr>
			

			<!-- Дата создания -->
			<tr id="row_create_date">
				<!-- Отступ перед -->
				<td><div class="entityempty">&nbsp;</div></td>
				<!-- Заголовок поля -->
				<td class="entitycaption" nowrap="nowrap" valign="top" width="15%">Дата</td>
				<!-- Поле -->
				<td class="entitytext" align="left" valign="middle" width="85%">
					<input value="<%= Util.toViewDate(data.get("create_date"))%>" style="width:12%" maxlength="10" name="create_date" id="create_date" type="text">
				</td>
				<!-- Отступ после -->
				<td><div class="entityempty">&nbsp;</div></td>
			</tr>
			
			<!-- Строка - отступ между полями -->
			<tr>
				<td class="entityrows_gap"></td>
			</tr>
			
			<!-- Время создания -->
			<tr id="row_create_time">
				<!-- Отступ перед -->
				<td><div class="entityempty">&nbsp;</div></td>
				<!-- Заголовок поля -->
				<td class="entitycaption_v_aligned" nowrap="nowrap" valign="top" width="10%">Время</td>
                <td class="entitytext" align="left" valign="middle" width="100%">
                    <input value="<%= Util.toViewTime(data.get("create_date"))%>" style="width:100%" maxlength="100" name="create_time" type="text">
                </td>
				<!-- Отступ после -->
				<td><div class="entityempty">&nbsp;</div></td>
			</tr>
			
			<!-- Строка - отступ между полями -->
			<tr>
				<td class="entityrows_gap"></td>
			</tr>

            <!-- Скрэмблер -->
            <tr id="row_scrambler">
                <!-- Отступ перед -->
                <td><div class="entityempty">&nbsp;</div></td>
                <!-- Заголовок поля -->
                <td class="entitycaption_v_aligned" nowrap="nowrap" valign="top" width="10%">Скрэмблер</td>
                <td class="entitytext" align="left" valign="middle" width="100%">
                    <input value="<%= data.get("scrambler")%>" style="width:100%" maxlength="100" name="scrambler" type="text">
                </td>
                <!-- Отступ после -->
                <td><div class="entityempty">&nbsp;</div></td>
            </tr>
		<!-- Конец таблицы группы полей -->
		</tbody></table>
	<!-- Строка с таблицей общей по всей ширины КОНЕЦ -->
	</td></tr>
<!-- Конец внешной таблицы -->	
</tbody></table>
<!-- Таблица с кнопками -->
</br>
</br>
</br>
<table width="100%"><tbody>
<tr>
<td width="48%" style="text-align:right;vertical-align:top;">
<input type="submit" id="btn-primary" class="btn-primary" value="Сохранить"></td>
<td width="4%"></td>
<td width="48%" style="text-align:left;">
<!-- TODO Сделать это поведение зависящим от типа открытия, закрывать так только если NEW если THIS то переходить на прошлый экран -->
<input type="submit" name="cancel" onclick="window.close(); return false;" value="Отмена" class="btn-primary" id="cancel" style="background:#999999; border-color:#000000">
</tr>
</tbody></table>
</br>
</form>
 </body></html>