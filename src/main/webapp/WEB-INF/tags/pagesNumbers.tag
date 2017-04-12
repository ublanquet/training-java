
<%@ attribute name="nbPages" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul class="pagination col-xs-10">
    <li>
        <a href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
        </a>
    </li>
    <c:if test="${nbPages < 60}"> <!-- TODO Defiling when too much pages -->
        <c:forEach begin="0" end="${nbPages}" var="i">
            <li id="p${i}"><a href="#">${i}</a></li>
        </c:forEach>
    </c:if>
    <c:if test="${nbPages >= 60}">
        <c:forEach begin="0" end="60" var="i">
            <li id="p${i}"><a href="#">${i}</a></li>
        </c:forEach>
    </c:if>
    <li>
        <a href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
        </a>
    </li>
</ul>