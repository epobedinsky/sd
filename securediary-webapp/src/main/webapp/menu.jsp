<%@ page import="org.ogai.core.Ctx" %>
<%@ page import="org.ogai.core.User" %>
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
<%
    Ctx ctx = new Ctx(request, response,  "showMenu");
    User currentUser = ctx.getUser();
    String role = currentUser.getRoles().get(0).getDisplayName().toString();
    String fullName = currentUser.getFullName();

%>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!--<meta name="msapplication-TileColor" content="#0077B5">-->
<meta name="application-name" content="Aquatour">
<link rel="stylesheet" type="text/css" href="css/login/css.css">
<link rel="stylesheet" type="text/css" href="css/login/css_004.css">
<title>Aquatour Webb</title>
<link rel="stylesheet" type="text/css" href="css/login/css_003.css">
<link rel="stylesheet" type="text/css" href="css/login/css_002.css">
<style type="text/css" charset="utf-8">

.nav-caption {
    color: #D7D7D7;
	padding-left: 10px;
	padding-right: 10px;
	display: block;
    padding-bottom: 14px;
    padding-top: 13px;
    text-decoration: none;
}

.menu_button {
    width:77px;
    height:73px;
    display:block;
    overflow:hidden;
}

.menu_button:hover {
    text-indent:-77px;
}
</style>
</head>
<!-- id отвечает за цвет фона -->
<body dir="ltr" class="guest v2  chrome-v5 chrome-v5-responsive sticky-bg guest js " id="pagekey-uas-consumer-login-internal">
<div id="header" class="global-header responsive-header nav-v5-2-header responsive-1 remote-nav" role="banner">
 <div id="top-header">
<!--<div class="wrapper">-->
<ul class="nav main-nav guest-nav" role="navigation">
   <li class="nav-item">
    <font class="nav-caption">
      <%=role%>
    </font>
  </li>
  <li class="nav-item">
    <font class="nav-caption">
        <%=fullName%>
    </font>
  </li>
  <li class="nav-item">
    <a  href="logout.do" class="nav-link" target="_top">Выйти</a>
  </li>
</ul>
 <!--</div>-->
 </div>
</div>
</br>
</br>
</br>
</br>
<table>
<tr>
    <!-- предзаказы -->
	<!--
    <td>
        <a href='#' class="menu_button">
            <img src="img/menu/preorder.png">
         </a>
	</td>
	-->
    <!-- Клиенты -->
    <td>
        <a href='#' class="menu_button">
            <img src="img/menu/clients.png">
         </a>
    </td>
	<!-- Добавить сюда новые пункты меню
	-->
<tr>
</table>
 </body></html>