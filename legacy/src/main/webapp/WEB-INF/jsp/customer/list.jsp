<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Legacy Customers</title>
</head>
<body>
<h1>Customer list</h1>
<% if (request.getParameter("debug") != null) { %>
<div>Debug mode: <%= request.getRemoteAddr() %></div>
<% } %>
<table border="1">
    <tr><th>ID</th><th>Name</th><th>Email</th><th>Active</th></tr>
    <c:forEach items="${customers}" var="customer">
        <tr>
            <td>${customer.id}</td>
            <td>${customer.fullName}</td>
            <td>${customer.email}</td>
            <td>${customer.active}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
