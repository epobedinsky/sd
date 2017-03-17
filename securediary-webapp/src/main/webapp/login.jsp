<%@ page import="org.ogai.util.WebUtil" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.ogai.command.sys.LoginCommand" %>
<%@ page import="java.util.HashMap" %>

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
	<meta name="msapplication-TileColor" content="#0077B5">
	<meta name="application-name" content="Aquatour">
	<link rel="stylesheet" type="text/css" href="css/login/css.css">
	<link rel="stylesheet" type="text/css" href="css/login/css_004.css">
	<title>Войти | Aquatour Webb</title>
	<link rel="stylesheet" type="text/css" href="css/login/css_003.css">
	<link rel="stylesheet" type="text/css" href="css/login/css_002.css">


</head>
<%
        //в параметрах приходят сообщения об ошибках
        //в виде - ключ - name поля, значение - сообщение об ошибке
        Map<String, String> errorMessages = new HashMap<String, String>();
        Map<String, String> tmpErrorMessages = WebUtil.getParams(request);

        //Удаляем все параметры не имеющие отношение к логину
        for (String key : tmpErrorMessages.keySet()) {
            if (key.equals(LoginCommand.LOGIN_PARAM_ERROR) || key.equals(LoginCommand.PASSWORD_PARAM_ERROR)
                    || key.equals(LoginCommand.LOGIN_CMD_PARAM_ERROR)) {
                errorMessages.put(key, tmpErrorMessages.get(key));
            }
        }
%>

<body dir="ltr" class="guest v2  chrome-v5 chrome-v5-responsive sticky-bg guest js " id="pagekey-uas-consumer-login-internal">
<div id="a11y-menu" class="a11y-skip-nav-container">
</div>
<div id="header" class="global-header responsive-header nav-v5-2-header responsive-1 remote-nav" role="banner">
	<div id="top-header">
		<div class="wrapper">
			<ul class="nav main-nav guest-nav" role="navigation">
			</ul>
		</div>
	</div>
	<div class="a11y-content">
	</div>
</div>

<div id="body" class="" role="main">
	<div id="page-title"><h1>Войти в Aquatour Web</h1></div>
	<div class="wrapper hp-nus-wrapper">
		<div id="global-error">
		</div>
		<div id="main" class="signin">
            <% //Если пришла какая-то общая ошибка с сервера, показываем ее в summary
                if (errorMessages.containsKey(LoginCommand.LOGIN_CMD_PARAM_ERROR)) { %>
                    <div id="yui-gen0"><div class="alert error" role="alert"><p><strong><%= errorMessages.get(LoginCommand.LOGIN_CMD_PARAM_ERROR)%></strong></p></div></div>
			<% //Если были любые другие ошибки, кроме общей, присланной с сервера, показываем общее сообщение об ошибке
                } else if (!errorMessages.isEmpty()) { %>
                    <div id="yui-gen0"><div class="alert error" role="alert"><p><strong>При заполнении формы были допущены ошибки. Проверьте и исправьте отмеченные поля.</strong></p></div></div>
            <% } %>
			<form action="login.do" method="POST" name="login" novalidate="novalidate" id="login_from" class="ajax-form">
				<ul>
					<li>
						<label for="login">Логин</label>
						<div class="fieldgroup">
							<% if (errorMessages.containsKey(LoginCommand.LOGIN_PARAM_ERROR)) {%>
                                <span class="error" id="login-error"><%= errorMessages.get(LoginCommand.LOGIN_PARAM_ERROR)%></span>
                            <%}%>
							<input <%= errorMessages.containsKey(LoginCommand.LOGIN_PARAM_ERROR) ? "class=\"error\"" : "" %> name="login" id="login" type="text">
						</div>
					</li>
					<li>
						<label for="password">Пароль:</label>
						<div class="fieldgroup">
                            <% if (errorMessages.containsKey(LoginCommand.PASSWORD_PARAM_ERROR)) {%>
                            <span class="error" id="password-error"><%= errorMessages.get(LoginCommand.PASSWORD_PARAM_ERROR)%></span>
                            <%}%>
							<input <%= errorMessages.containsKey(LoginCommand.PASSWORD_PARAM_ERROR) ? "class=\"error\"" : "" %> name="password" id="password" type="password">
						</div>
					</li>
					<li class="button">
						<input name="signin" value="Войти" class="btn-primary" id="btn-primary" type="submit"><div style="line-height: 24px;" class="progress-indicator"></div>
					</li>
				</ul>
				<div class="screen"></div></form>
		</div>
	</div>
</div>

</body></html>