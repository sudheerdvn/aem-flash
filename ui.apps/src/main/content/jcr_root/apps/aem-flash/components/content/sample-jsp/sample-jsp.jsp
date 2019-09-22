<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

Sample JSP

<c:set var = "myDate" value="03-07-2019 02:10:24"/>
<c:out value="${myDate}"/>

<fmt:parseDate value = "${myDate}" var = "parsedMyDate" pattern = "dd-MM-yyyy hh:mm:ss" />
<fmt:formatDate type = "time" value = "${parsedMyDate}" />
