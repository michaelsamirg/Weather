<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Update Daily Notes</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
	<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
	  
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">

   <security:authorize access="hasRole('ROLE_ADMIN')">
   		
   		<form:form method="POST" modelAttribute="dailyNotesForm" class="form-signin" action="${contextPath}/dailyNotes/submit">
        <h2 class="form-signin-heading">Update Daily Notes</h2>
        <spring:bind path="value1To10">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:textarea path="value1To10" class="form-control" placeholder="Notes for temp > 1 and <= 10"
                            autofocus="true"></form:textarea>
                <form:errors path="value1To10"></form:errors>
            </div>
        </spring:bind>

		<spring:bind path="value10To15">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:textarea path="value10To15" class="form-control" placeholder="Notes for temp > 10 and <= 15"
                            autofocus="true"></form:textarea>
                <form:errors path="value10To15"></form:errors>
            </div>
        </spring:bind>
        
        <spring:bind path="value15To20">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:textarea path="value15To20" class="form-control" placeholder="Notes for temp > 15 and <= 20"
                            autofocus="true"></form:textarea>
                <form:errors path="value15To20"></form:errors>
            </div>
        </spring:bind>

        <spring:bind path="valueGt20">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:textarea path="valueGt20" class="form-control" placeholder="Notes for temp > 20"></form:textarea>
                <form:errors path="valueGt20"></form:errors>
            </div>
        </spring:bind>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
    </form:form>
   
		<h2><a href="${contextPath}/dailyNotes/list">List Previous Notes</a></h2>
		<h2><a href="${contextPath}/welcome">Back to dashboard</a></h2>
	</security:authorize>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
