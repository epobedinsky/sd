<%@ page import="org.ogai.core.Ctx" %>
<%@ page import="org.ogai.command.sys.GoCommand" %>
<html><head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="expires" content="now">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <title>Aquatour</title>
    <link rel="stylesheet" type="text/css" href="css/main/area.css">
    <link rel="stylesheet" type="text/css" href="css/main/area_002.css">
</head>
<body style="overflow:auto">
<table cellpadding="0" cellspacing="0" border="0" height="100%" width="100%">

    <tbody><tr>
        <td width="15%">
            <iframe id="tagsList" name="tagsList" src="tagslist.do" style="height:100%;width:100%" frameborder="0" scrolling="yes"></iframe>
        </td>
        <td width="85%">
            <iframe id="mainContent" name="mainContent" src="viewrec.do?id=<%= Ctx.get().getRequestParams().get(GoCommand.PARAM_ID) %>" style="width:100%;height:100%" frameborder="0" scrolling="yes"></iframe>
        </td>
    </tr></tbody></table>


</body></html>