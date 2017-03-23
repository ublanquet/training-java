<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"
           uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="list" type="java.util.ArrayList" required="true" %>
<%@ attribute name="filteredCount" required="false" %>

<c:if test="${not empty filteredCount}">
    <input type="hidden" id="filteredCount" value="${filteredCount}">
</c:if>
<c:forEach items="${list}" var="item">
    <tr>
        <td class="editMode">
            <input type="checkbox" name="cb" class="cb" value="${item.id}">
        </td>
        <td>
            <a href="editcomputer?id=${item.id}" onclick="">${item.name}</a>
        </td>
        <td>${item.introduced}</td>
        <td>${item.discontinued}</td>
        <td>${item.companyName}</td>
    </tr>
</c:forEach>
