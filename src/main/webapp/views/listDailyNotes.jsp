<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %> 

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

    <title>List Daily Notes</title>

	<script type="text/javascript" src="${contextPath}/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="${contextPath}/resources/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${contextPath}/resources/js/jquery-ui.js"></script>
	<script type="text/javascript" src="${contextPath}/resources/js/jquery.jqGrid.min.js"></script>
	
	<link rel="stylesheet" href="${contextPath}/resources/css/jquery-ui.css">
 	<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
	<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
	<link href="${contextPath}/resources/css/ui.jqgrid.css" rel="stylesheet">
	<link href="${contextPath}/resources/css/ui.jqgrid-bootstrap.css" rel="stylesheet">
	<link href="${contextPath}/resources/css/ui.jqgrid-bootstrap-ui.css" rel="stylesheet">
	
  	<script type="text/javascript">
  	
  		var grid = null;
  	
  		$( function() {
    		$( "#startDatepicker" ).datepicker();
    		$( "#endDatepicker" ).datepicker();
    		
    		$("#startDatepicker").css("z-index", "1000000");
    		$("#endDatepicker").css("z-index", "100000");
    		$("#gbox_list2").css("z-index", "1");
    		
    		$( "#search" ).click(function(){
    				
    			var data = {"startDate": $("#startDatepicker").val(), "endDate": $("#endDatepicker").val()};
    			
    			if(grid == null)
    			{
    				grid = jQuery("#list2").jqGrid({ 
    	      			url:'<%=request.getContextPath()%>/dailyNotes/search', 
    	      			postData: data,
    	      			datatype: "json", 
    	      			colNames:['Date', 'Category', 'Notes'], 
    	      			colModel:[  
    	      			           {name:'date',index:'date', width:90, formatter: dateFmatter}, 
    	      			           {name:'category',index:'category', width:150, formatter: categoryFmatter},  
    	      			           {name:'notes',index:'notes', width:200, sortable:false} ], 
    	      			rowNum:10, 
    	      			width: 600,
    	      			rowList:[10,20,30], 
    	      			sortname: 'date', 
    	      			viewrecords: true, 
    	      			sortorder: "desc", 
    	      			caption:"Previous Notes" });
    				
    				$("#gbox_list2").css("z-index", "1");
    			}
    			else
    			{
    				jQuery('#list2').jqGrid('clearGridData');
        			jQuery('#list2').jqGrid('setGridParam', {postData: data});
        			jQuery('#list2').trigger('reloadGrid');
    			}
    				
    		});
    		
    
    		
    		
  		} );
  		
  		function dateFmatter (cellvalue, options, rowObject)
  		{
  		   var date = new Date(cellvalue);
  		   return date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear()
  		}
  		
  		function categoryFmatter (cellvalue, options, rowObject)
  		{
  			if(cellvalue == 'TEMP_1_10')
  				return '1 < temperature <= 10';
  			else if(cellvalue == 'TEMP_10_15')
  				return '10 < temperature <= 15';
  			else if(cellvalue == 'TEMP_15_20')
  				return '15 < temperature <= 20';
  			else if(cellvalue == 'TEMP_GT_20')
  				return 'temperature > 20';
  				
  		   return cellvalue
  		}
  		
  	</script>
  	
  	
	  
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">

   <security:authorize access="hasRole('ROLE_ADMIN')">
   		<h2>List Daily Notes</h2>
   
   		<p>Start Date: <input type="text" id="startDatepicker"  style="z-index: 10000">
   		    End Date: <input type="text" id="endDatepicker"  style="z-index: 100001"></p>
   
   		<p> <button type="button" id="search" value="search" style="width:80px; height: 40px;">search</button> </p>
   
   		<table id="list2" style="z-index: 1"></table>
		<div id="pager2"></div>
   
   		<p><div id="jsGrid"></div></p>
		<h2><a href="${contextPath}/dailyNotes/update">Update Daily Notes</a></h2>
		<h2><a href="${contextPath}/welcome">Back to dashboard</a></h2>
	</security:authorize>

</div>
<!-- /container -->

</body>
</html>
