<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 20/03/17
  Time: 13:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"
           uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
testetstetdsqf23 ${test}

<c:forEach items="${list}" var="item">
    ${item}<br>
</c:forEach>

</body>
</html>
