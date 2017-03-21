
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"
           uri="http://java.sun.com/jsp/jstl/fmt" %>

<input type="hidden" id="filteredCount" value="${filteredCount}">

<c:forEach items="${list}" var="item">
    <tr>
        <td class="editMode">
            <input type="checkbox" name="cb" class="cb" value="0">
        </td>
        <td>
            <a href="editComputer.html" onclick="">${item.getName()}</a>
        </td>
        <td>${item.getIntroduced()}</td>
        <td>${item.getDiscontinued()}</td>
        <td>${item.getCompany().getName()}</td>
    </tr>
</c:forEach>
