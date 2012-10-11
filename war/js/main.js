function doQuery(queryType, modelType) {
	var url = "/" + modelType + "/" + queryType;
	var callback = "do" + ucwords(queryType);
	console.log("doQuery: requesting " + url + " & sending data to " + callback);
	$.post(
			url,
			{
				userKey: userkey
			},
			function (data) {
				console.log("doQuery: data received");
				if (!data.success) {
					console.log("doQuery Error: " + url + " " + data);
					return;
				}
				console.log("doQuery: got a response & sending data to " + callback + " with the param " + modelType);
				callbackFunction = eval(callback);
				callbackFunction(modelType, data);
			}
		);
}

function getRead(modelType){
	doQuery('read', modelType);
}

function doRead(modelType, data){
	var action = 'List';
	console.log("doRead: " + modelType + " printing title and table");
	$('#pdfsp_admin_title').text(ucwords(modelType) + " " + action);
//	printTable('#' + modelType + action, data);
	printTable("#object" + action, data);
}

function printTable(tablename, data){
	console.log("printTable: " + tablename + " printing!");
	$(tablename).empty();
	if(data==null || data.total<=0){
		console.log("printTable Error: " + tablename + " " + data);
		$(tablename).append($('<tr>').append($('<th>').text('Empty result')));
		return;
	}
	//header
	var row = $('<tr>');
	var totFields = 0;
	jQuery.each(data.result[0], function(key, value){
		row.append($('<th>').text(ucwords(key)));
		totFields++;
	});
	$(tablename).append($('<thead>').append(row));

	//body
	$(tablename).append($('<tbody>'));
	jQuery.each(data.result, function(index, item){
//		console.log(item);
		row = $('<tr>').attr('class', 'pdfsp_admin_row_' + index%2);
		jQuery.each(item, function(key, value){
//			console.log(key + ": " + value);
			row.append($('<td>').text(value));
		});
		$(tablename).find('tbody').append(row);
	});	//footer
	$(tablename).append($('<tfoot>').append($('<tr>').append($('<th>').attr('colspan', totFields).text(data.total + ' results'))));
}

function getUrlVars() {
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
}

function ucwords (str) {
    return (str + '').replace(/^([a-z])|\s+([a-z])/g, function ($1) {
        return $1.toUpperCase();
    });
}

function lcwords (str) {
    return (str + '').replace(/^([a-z])|\s+([a-z])/g, function ($1) {
        return $1.toLowerCase();
    });
}

function extractUri (str) {
    return lcwords((str + '').slice((str + '').indexOf('#') + 1));
}

$(document).ready(function () {
	jsonResult = null;
	userkey = getUrlVars()["userKey"];
	modelType = getUrlVars()["modelType"];
	
	$('#pdfsp_admin_header_menu a').each(function(index, item){
		$(item).click(function(){
			console.log('click on ' + item);
			getRead(extractUri(item));
			return false;
		});
	});

	$( "#filters_created_at_from" ).datepicker();
	$( "#filters_created_at_to" ).datepicker();
	
	//FIXME remove default value
	if(userkey==undefined) userkey="876";
	if(modelType==undefined) modelType="order";
	console.log("userkey: "+ userkey);
	console.log("modelType: "+ modelType);
	getRead(modelType);
});