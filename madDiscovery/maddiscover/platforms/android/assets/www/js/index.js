document.addEventListener('deviceready', onDeviceReady, false);
var eventId;
var db;

function onDeviceReady() {
	console.log('onDeviceReady');
	initDB();
}

function initDB() {
	db = window.sqlitePlugin.openDatabase("mad_discover_phonegap.db", "9.0", "madDiscovery", 200000);
	db.transaction(populateDB, populateError, populateSuccess);
}

function populateDB(tx) {
	tx.executeSql("CREATE TABLE IF NOT EXISTS `events` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `venueId` VARCHAR(255), `eventName` VARCHAR(255), `createDate` DATETIME, `startDate` DATETIME, `dateOfEvent` VARCHAR(255), `organizer` BIGINT, `remark` VARCHAR(255));");
    tx.executeSql("CREATE TABLE IF NOT EXISTS `report` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `eventId` VARCHAR(255), `text` VARCHAR(10000));");
	tx.executeSql("CREATE TABLE IF NOT EXISTS `image` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `eventId` VARCHAR(255), `url` VARCHAR(10000));");
}

function populateSuccess(tx) {
	db.transaction(queryDB, errorCB);
}

function populateError(err) {
}

function errorCB(err) {
	console.log(err);
}

function eventDetails(){
    getEventDetail("details");
    getReportList();
    loadImage();
}

function eventUpdate(){
    getEventDetail("update");
}

function queryDB(tx) {
    tx.executeSql('SELECT * FROM events ORDER BY eventName', [], querySuccess, errorCB);
}

// Query success
function querySuccess(tx, results) {
	var listView = document.getElementById('listEvent');
	if(listView != undefined){
	listView.innerHTML = '';
    	var length = results.rows.length;
    	for (var i=0; i<length; i++) {
    		var row = results.rows.item(i);
    		var date = row['startDate']!=null?row['startDate'].replace("00:00:00 GMT+07:00",""):'N/A';
    		listView.innerHTML += '<div class="divlist" id="' + row['id'] + '" onclick="listItemClick('+row['id']+')"><h3>'+row["eventName"]+'</h3> <span>' + date + '</span></div>';
    	};
    }

}

function errorCB(err) {
	console.log("Error processing SQL: " + err.message);
}

function insertSuccess() {
	db.transaction(queryDB, errorCB);
}

function validationcheck() {
}

function duplicate(tx, results){
	if(results.rows.length > 0)
	var row = results.rows.item(0);
	console.log("event name:" +row["eventName"]);
	return row["eventName"];
}

function listItemClick(e) {
	window.localStorage.setItem("eventId",e);
	window.location.href = "#details";
	eventDetails();
}

function create(){
	window.location.href = "create.html"
}

function list(){
	window.location.href = "list.html"
}

function getEventDetail(type){
	if(type == "details")
		db.transaction(queryEvent, errorCB);
	if(type == "update")
		db.transaction(queryEventUpdate, errorCB);
}

function queryEvent(tx){
	var id=window.localStorage.getItem("eventId");
	tx.executeSql('SELECT * FROM events where id='+id, [], initDetails, errorCB);
}

function queryEventUpdate(tx){
	var id=window.localStorage.getItem("eventId");
	tx.executeSql('SELECT * FROM events where id='+id, [], initUpdate, errorCB);
}

function initDetails(tx, results){
	var ename = document.getElementById("ename_de");
	var location = document.getElementById("location_de");
	var date = document.getElementById("date_de");
	var time = document.getElementById("time_de");
	var org = document.getElementById("org_de");

	if(results.rows.length > 0)
	var row = results.rows.item(0);
	var datefm = row['startDate']!=null?row['startDate'].replace("00:00:00 GMT+07:00",""):'N/A';
	ename.innerHTML = row["eventName"]!=null?row["eventName"]:"";
	date.innerHTML = datefm;
	time.innerHTML = row["dateOfEvent"]!=null?row["dateOfEvent"]:"";
	location.innerHTML = row["venueId"];
	org.innerHTML = row["organizer"];
}

function initUpdate(tx, results){
	var ename = document.getElementById("ename_up");
	var location = document.getElementById("location_up");
	var date = document.getElementById("date_up");
	var time = document.getElementById("time_up");
	var org = document.getElementById("org_up");

	if(results.rows.length > 0)
	var row = results.rows.item(0);
	var datefm = row['startDate']!=null?row['startDate'].replace("00:00:00 GMT+07:00",""):'N/A';
	ename.value = row["eventName"]!=null?row["eventName"]:"";
	date.value = datefm;
	time.value = row["dateOfEvent"]!=null?row["dateOfEvent"]:"";
	location.value = row["venueId"];
	org.value = row["organizer"];
}

function saveNew(){
	var ename = document.getElementById("ename").value;
    var date = document.getElementById("date").value;
    var location = document.getElementById("location").value;
    var isValid = validate(ename,date,location);
    if(isValid.length>0)
    	alert(isValid);
    else{
	   	db.transaction(checkDuplicate,errorCB);
	}
}

function insertSQL(tx){
	var ename = document.getElementById("ename").value;
	var date = document.getElementById("date").value;
	var time = document.getElementById("time").value;
	var org = document.getElementById("org").value;
	var location = document.getElementById("location").value;

	tx.executeSql("insert into events (eventName,startDate,dateOfEvent,venueId,organizer) "
	+ "values('"+ename+"','"+date+"','"+time+"','"+location+"','"+org+"')",[],saveSuccess,errorCB);
}

function checkDuplicate(tx) {
	var fname = document.getElementById("ename").value;
 	tx.executeSql("SELECT * FROM events where eventName='"+ fname + "'", [], checkDuplicateSuccess, errorCB);
}

function checkDuplicateSuccess(tx, results) {
	var length = results.rows.length;
	if(length > 0){
		alert('Duplicate event name!!!');
		return;
	}
	else
		db.transaction(insertSQL, errorCB, saveSuccess);
}

function saveSuccess(tx){
	db.transaction(queryDB, errorCB);
	window.location.href = "#list";
}

function validate(ename,date,location){
	var valid = "";
	if(ename.length<=0)
		valid += "Event name is mandatory! \r";
	if(date.length<=0)
    	valid += "Event start date is mandatory! \r";
	if(location.length<=0)
		valid += "Location is mandatory! \r";

	return valid;
}

function cancelCreate(){
	window.location.href = "#list"
}

function loadEdit(){
	window.location.href ="#update";
	eventUpdate();
}

function cancelEdit(){
	window.location.href = "#details";
}

function update(){
	var ename = document.getElementById("ename_up").value;
        var date = document.getElementById("date_up").value;
        var location = document.getElementById("location_up").value;
        var isValid = validate(ename,date,location);
        if(isValid.length>0)
        	alert(isValid);
        else{
    	   	db.transaction(checkDuplicateUpdate,errorCB);
    	}
}

function checkDuplicateUpdate(tx){
	var id=window.localStorage.getItem("eventId");
	var fname = document.getElementById("ename_up").value;
    tx.executeSql("SELECT * FROM events where eventName='"+ fname + "' and id!= "+id , [], checkDuplicateUpdateSuccess, errorCB);
}

function checkDuplicateUpdateSuccess(tx, results) {
	var length = results.rows.length;
	if(length > 0){
		alert('Duplicate event name!!!');
		return;
	}
	else
		db.transaction(updateEvent, errorCB, saveSuccess);
}

function updateEvent(tx){
	var ename = document.getElementById("ename_up").value;
    var date = document.getElementById("date_up").value;
    var time = document.getElementById("time_up").value;
    var org = document.getElementById("org_up").value;
    var location = document.getElementById("location_up").value;
	var id=window.localStorage.getItem("eventId");

	tx.executeSql("update events set eventName = '"+ename
	+"',startDate = '"+date
	+"',dateOfEvent ='"+time
	+"',venueId ='"+location+"'"
	+",organizer = '"+org
	+"' where id="+id ,[],updateSuccess,errorCB);
}

function updateSuccess(){
	eventDetails();
}

function onConfirm(buttonIndex) {
	if(buttonIndex ==1)
		deleteEvent();
}

function showConfirm() {
	navigator.notification.confirm(
		'Delete this event!',  // message
		onConfirm,              // callback to invoke with index of button pressed
		'Delete',            // title
		['Delete','Cancel']          // buttonLabels
	);
}

function deleteEvent(){
	db.transaction(sqlDelete,errorCB,delSuccess);
}

function sqlDelete(tx){
	var id=window.localStorage.getItem("eventId");
	tx.executeSql("Delete from events where id = " + id,[],queryDB,errorCB);
}

function delSuccess(tx){
	window.location.href = "#list";
}

function report(){
	navigator.notification.prompt(
		'',  // message
		addReport,              // callback to invoke with index of button pressed
		'Add Report',            // title
		['OK','Cancel']          // buttonLabels
	);
}

function addReport(results){
	if(results.buttonIndex==1){
		if(results.input1.length ==0 )
			alert("Please enter report");
		else{
			window.localStorage.setItem("report",results.input1);
			saveReport();
		}
	}

}

function saveReport(){
	db.transaction(sqlAddReport,errorCB,addReportSuccess);
}

function sqlAddReport(tx){
	var eventId = window.localStorage.getItem("eventId");
	var report = window.localStorage.getItem("report");
	tx.executeSql("Insert into report (eventId,text) values("+eventId+",'"+report+"')", [],null, errorCB);
}

function addReportSuccess(){
	getReportList();
}

function getReportList(){
	db.transaction(sqlReport,errorCB);
}

function sqlReport(tx){
	var eventId = window.localStorage.getItem("eventId");
	tx.executeSql("Select * from report where eventId="+eventId,[],function(tx,results){
		var length = results.rows.length;
		var listView = document.getElementById('reports');
		listView.innerHTML = "";
		for (var i=0; i<length; i++) {
			var row = results.rows.item(i);
			var text = row['text'];
			listView.innerHTML += '<div class="divlist" id="' + row['id'] + '">'+row["text"]+'</div>';
		};
	},errorCB);
}

function addPhoto(){
	window.imagePicker.getPictures(
    	function(results) {
    		var rs="";
    		for (var i = 0; i < results.length; i++) {
				console.log('Image URI: ' + results[i]);
				rs+=results[i]+",";
            }
            window.localStorage.setItem("urlImg",rs.substring(0, rs.lastIndexOf(",")));
    		saveImage();
    	}, function (error) {
    		console.log('Error: ' + error);
    	}
    );
}

function saveImage(){
	db.transaction(sqlSaveImg,errorCB,loadImage);
}

function sqlSaveImg(tx){
	var eventId = window.localStorage.getItem("eventId");
	var image = window.localStorage.getItem("urlImg").split(",");
	for (var i = 0; i < image.length; i++) {
    	console.log('Image URI: ' + image[i]);
    	tx.executeSql("Insert into image (eventId,url) values("+eventId+",'"+image[i]+"')", [],null, errorCB);
    }
}

function loadImage(){
	db.transaction(loadEventImages,errorCB);
}

function loadEventImages(tx){
	var eventId = window.localStorage.getItem("eventId");
	tx.executeSql("Select * from image where eventId="+eventId,[],function(tx,results){
		var length = results.rows.length;
		var listView = document.getElementById('images');
		listView.innerHTML = "";

		for (var i=0; i<length; i++) {
			var row = results.rows.item(i);
			console.log(row['url']);
			var sr = row['url'];
			listView.innerHTML += "<a href='#myPopup' data-rel='popup' data-position-to='window' id='zoomImg'"
			+"> <img src='"+sr+"' height='60px' width='60px' onclick='setImageSource(this.src)'/></a>";
		};

	},errorCB);
}

function setImageSource(_src){
	document.getElementById("popup").src = _src;
}