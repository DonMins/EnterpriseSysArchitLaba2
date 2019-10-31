<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="../resources/css/font.css" type="text/css">
    <title> Игра быки и коровы </title>
    <link href="${contextPath}/resources/css/font.css" rel="stylesheet">
    <meta id="_csrf_token" value="${_csrf.token}"/>

    <script src="http://code.jquery.com/jquery-2.2.4.js"
            type="text/javascript"></script>
    <script src="${contextPath}/resources/js/app-ajax.js" type="text/javascript"></script>
    <script>
        var urlAttempt = "${contextPath}/newAttempt";
        var urlHistory = "${contextPath}/history";
    </script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</head>

<body>
<ul>
    <li class="rating"><a href="${contextPath}/rating">Рейтинг игроков</a></li>
    <li class="rating"><a href="${contextPath}/xmlHistory">XML</a></li>
    <li class="rating"><a href="${contextPath}/jsonHistory">JSON</a></li>
    <button style="float:right" class="button" type="submit" onclick="document.forms['logoutForm'].submit()">Выйти
    </button>

    <c:if test="${pageContext.request.userPrincipal.name != null}">
    <form id="logoutForm" method="POST" action="${contextPath}/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <p class="userInput"> Вы вошли как: ${pageContext.request.userPrincipal.name}
        </c:if>

</ul>
<h1 style="margin-top: 50px"> Игра быки и коровы </h1>
<h2> Я загадал число, попробуй угадать его... </h2>
<table>
    <tr>
        <th style="width: 800px"><textarea readonly id="textarea"></textarea></th>
        <th style="width: 800px"><textarea readonly id="textareaHistory"></textarea></th>
    </tr>
    <tr>рррт
        <th>
            <div class="text">Поле для ввода числа:</div>
            <input class="input" type="text" id="inputNumber" maxlength='4' minlength='4'
                   placeholder="Введите 4-х значное число" onkeyup="return check(this);"
                   onchange="return check(this); " required/></th>
<%--        <form id="xmlForm" method="POST" action="${contextPath}/logout">--%>
<%--            <th> <button class="button" type="submit" id="xml">Посмотреть ответ в XML</button> </th>--%>
<%--        </form>--%>

        <form id="xmlForm" method="POST" action="${contextPath}/xmlHistory">
<%--            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>--%>
            <button class="button" type="submit" id="xml">Посмотреть ответ в XML</button>
        </form>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </tr>
    <tr>
        <th><span id="error"> </span></th>
        <th>
            <button class="button" type="submit" id="history">Посмотреть историю</button>
        </th>
    </tr>
    <tr>
        <th>
            <button class="button" type="submit" id="toSend">Отправить</button>
        </th>
        <th>
            <button class="button" type="submit" id="clearHistory">Очистить историю</button>
        </th>
    </tr>
</table>
</body>
</html>
