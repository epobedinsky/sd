<html>
<head>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="expires" content="now">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <title>Aquatour</title>
    <link rel="stylesheet" type="text/css" href="css/main/area.css">
    <link rel="stylesheet" type="text/css" href="css/main/area_002.css">

</head>
<body style="overflow:auto">
<table id="maintable" cellpadding="0" cellspacing="0" border="0" height="100%" width="100%">

    <tbody><tr height="1%" border="0">
        <td colspan="2" valign="top">
            <!-- Переделать чтоб меню строилось серверным экшном -->
            <iframe id="menu" name="menu" src="menu.jsp" style="height:100%;width:100%" frameborder="0" scrolling="no"></iframe>
        </td>
    </tr>

    <tr height="99%" border="0">
        <td height="100%" valign="top" width="100%">
            <!-- Переделать чтоб страничка отображаемая в main зависела от того, куда перешли -->
            <!--<iframe id="main" name="main" src="subMenuPage.jsp" style="width:100%;height:100%" frameborder="0" scrolling="no"></iframe>-->
            <iframe id="main" name="main" src="sampleGrid.jsp" style="width:100%;height:100%" frameborder="0" scrolling="no"></iframe>
        </td>
    </tr></tbody></table>


</body></html>