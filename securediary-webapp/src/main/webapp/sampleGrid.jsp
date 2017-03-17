<%@ page import="org.ogai.grid.GridCommand" %>
<%@ page import="com.securediary.calendar.CalendarGridProxyFactory" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Aquatour</title>
<link rel="stylesheet" type="text/css" href="css/flexigrid/flexigrid.pack.css">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script type="text/javascript" src="js/flexigrid.pack.js"></script>
<link href="smoothness/jquery-ui-1.9.2.custom.min.css" rel="stylesheet">
<script src="js/jquery-ui-1.9.2.custom.min.js"></script>
<script>
	$(function() {
		$("#add_new").button();
	});
</script>
</head>
<body style="background-color: #E9E9E9">
<!-- Форма для передачи скрытых параметров, например имени грида на сервер -->
<form id="sform">
	<p>
    <input type="hidden" name="<%=GridCommand.GRIDNAME_PARAM%>" value="<%=CalendarGridProxyFactory.NAME%>" /><br />
</form>
<!-- Кнопки грида -->
<button id="add_new" onclick="window.open('newrec.do', '_blank'); return false;" style="font-size: 12px;font-family: Arial,sans-serif;text-decoration: none !important;">+</button></td>
</br>
<table id="flex1" style="display:none"></table>
<script type="text/javascript">
$("#flex1").flexigrid({
	url: 'grid.do',
	dataType: 'json',
	colModel : [
        {display: 'Дата', name : 'create_date', width : 200, sortable : true, align: 'left'},
        {display: 'Заголовок', name : 'title', width : 200, sortable : true, align: 'left'}
        //{display: '', name : 'delete_cmd', width : 200, sortable : true, align: 'left'}
		],
	sortname: "create_date",
	sortorder: "asc",
	usepager: true,
	useRp: true,
    onSubmit: addFormData,
	rp: 15,
    height: 300

});



//This function adds paramaters to the post of flexigrid. You can add a verification as well by return to false if you don't want flexigrid to submit

function addFormData(){
	//passing a form object to serializeArray will get the valid data from all the objects, but, if the you pass a non-form object, you have to specify the input elements that the data will come from
	var dt = $('#sform').serializeArray();
	$("#flex1").flexOptions({params: dt});
	return true;
}
</script>
</body>
</html>