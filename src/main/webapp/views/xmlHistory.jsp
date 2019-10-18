<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@page contentType="text/html;charset=UTF-8" language="java"%>

<html>
<head>
    <title>XML</title>
</head>
<body>

<div class="container center-block">
    <h1>XML</h1>
    ${xml}

</div>
</body>
</html>
