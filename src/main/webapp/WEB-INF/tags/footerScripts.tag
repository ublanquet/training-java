<%@ attribute name="validator" required="false" %>
<%@ attribute name="dashboard" required="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<c:if test="${not empty validator}">
    <script src="../js/validate.js"></script>
</c:if>
<c:if test="${not empty dashboard}">
    <script src="../js/dashboard.js"></script>
</c:if>