<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"
           uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Computer Database</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="utf-8">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="${pageContext.request.contextPath}/css/font-awesome.css" rel="stylesheet" media="screen">
    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
<header class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <a class="navbar-brand" href="/dashboard"> Application - Computer Database </a>
    </div>
</header>

<section id="main">
    <c:if test="${!messageHide}">
        <div class="alert alert-${messageLevel} alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <strong>${messageLevel} </strong> ${message}
        </div>
    </c:if>
    <div class="container">
        <h1 id="homeTitle">
            ${totalCount} <spring:message code="dashboard.find" />
        </h1>
        <input type="hidden" id="totalCount" value="${totalCount}">
        <div id="actions" class="form-horizontal">
            <div class="pull-left">
                <form id="searchForm" action="" class="form-inline" >

                    <input type="search" id="searchbox" name="search" class="form-control" placeholder="<spring:message code="dashboard.searchPh" />" />
                    <input type="button" id="searchsubmit" value="<spring:message code="dashboard.search" />"
                           class="btn btn-primary"/>
                </form>
            </div>
            <div class="pull-right">
                <a class="btn btn-success" id="addComputer" href="/computer/add"><spring:message code="dashboard.add" /></a>
                <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();"><spring:message code="dashboard.edit" /></a>
            </div>
        </div>
    </div>

    <form id="deleteForm" action="#" method="POST">
        <input type="hidden" name="selection" value="">
    </form>

    <div class="container" style="margin-top: 10px;">
        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <!-- Variable declarations for passing labels as parameters -->
                <!-- Table header for Computer Name -->

                <th class="editMode" style="width: 60px; height: 22px;">
                    <input type="checkbox" id="selectall"/>
                    <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                </th>
                <th>
                    <spring:message code="dashboard.th.computer" /> <span id="order_computer.name" class="ordering glyphicon glyphicon-sort"></span>
                </th>
                <th>
                    <spring:message code="dashboard.th.intro" /> <span id="order_introduced" class="ordering glyphicon glyphicon-sort"></span>
                </th>
                <!-- Table header for Discontinued Date -->
                <th>
                    <spring:message code="dashboard.th.disco" /> <span id="order_discontinued" class="ordering glyphicon glyphicon-sort"></span>
                </th>
                <!-- Table header for Company -->
                <th>
                    <spring:message code="dashboard.th.company" /> <span id="order_company.name" class="ordering glyphicon glyphicon-sort"></span>
                </th>

            </tr>
            </thead>
            <!-- Browse attribute computers -->
            <tbody id="results">
            <my:dashboard list="${list}"></my:dashboard>
            </tbody>
        </table>
    </div>
</section>

<footer class="navbar-fixed-bottom">
    <div class="container text-center">
        <my:pagesNumbers nbPages="${totalPages}"></my:pagesNumbers>

        <div class="btn-group btn-group-sm pull-right top-right col-xs-2" role="group">
            <button type="button" class="btn btn-default nbEntries active">10</button>
            <button type="button" class="btn btn-default nbEntries">50</button>
            <button type="button" class="btn btn-default nbEntries">100</button>
        </div>
    </div>
</footer>
<script>
    var contextPath = "${pageContext.request.contextPath}" + "";
</script>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>

</body>
</html>