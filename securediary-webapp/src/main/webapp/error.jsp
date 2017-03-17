<%@ page import="org.ogai.util.Util" %>
<%@ page import="org.ogai.view.ErrorView" %>
<%--
  Created by IntelliJ IDEA.
  User: epobedinskydb
  Date: 29.03.14
  Time: 15:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ошибка</title>
</head>
<%
    String details = request.getParameter(ErrorView.RP_ERROR_DETAILS);
    boolean isDetailedMessage = Util.isNotEmpty(details);
%>
<body>
    <H1>Просим прощения, возникла ошибка в работе системы.</H1>
    <br>
    <H1>Не стоит отчаиваться, зафиксируйте текущее время, свой логин и обратитесь к разработчику</H1>
    <% if (isDetailedMessage) { %>
        <br>
        <H1>Подробности:</H1>
        <H1><%= details %></H1>
    <% } %>
</body>
</html>