
<%@ attribute name="nbPages" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul class="pagination">
    <li>
        <a href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
        </a>
    </li>
    <c:forEach begin="0" end="${nbPages}" var="i">
        <li id="p${i}"><a href="#">${i}</a></li>
    </c:forEach>
    <li>
        <a href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
        </a>
    </li>
</ul>