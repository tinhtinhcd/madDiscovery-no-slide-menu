document.addEventListener('deviceready', onDeviceReady, false);
var eventId;
var db;
var tx;
var _seft = this;

document.addEventListener("DOMContentLoaded", function(event) {

});

function onDeviceReady() {
	console.log('onDeviceReady');
	initDB();
}

function initDB() {
	db = window.sqlitePlugin.openDatabase("mad_discover.db", "8.0", "madDiscovery", 200000);
	db.transaction(populateDB, populateError, populateSuccess);
}

function populateDB(tx) {
	_seft.tx = tx;
	tx.executeSql("CREATE TABLE IF NOT EXISTS `events` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `venueId` BIGINT, `eventName` VARCHAR(255), `createDate` DATETIME, `startDate` DATETIME, `dateOfEvent` VARCHAR(255), `organizer` BIGINT, `remark` VARCHAR(255));");
    tx.executeSql("CREATE TABLE IF NOT EXISTS `organizers` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `name` VARCHAR(255), `mobile` VARCHAR(255), `email` VARCHAR(255), `address` VARCHAR(255), `about` VARCHAR(10000) );");
    tx.executeSql("CREATE TABLE IF NOT EXISTS `venue` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `name` VARCHAR(255), `address` VARCHAR(255), `latitude` DOUBLE, `longitude` DOUBLE, `postal_code` VARCHAR(255) );;");
}

function populateSuccess(tx) {

	var detailForm = document.getElementById("details-from")
    	if(detailForm != null)
    		getEventDetail();
    	else
    		db.transaction(queryDB, errorCB);
}

function populateError(err) {
}

function errorCB(err) {
}

function insertDB(tx) {
//	var fname = document.getElementById("fname").value;
//	var lname =  document.getElementById("lname").value;
//	var age = document.getElementById("age").value;
//	var uname =document.getElementById("username").value;
//	var pwrd = document.getElementById("psw").value;
//	tx.executeSql("INSERT INTO Users (firstname,lastname,age,username,password) VALUES ('"+ fname +"','"+ lname +"' , "+ age+", '"+ uname +"','"+ pwrd +"' )");
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
	alert("Error processing SQL: " + err);
}

function insertSuccess() {
	db.transaction(queryDB, errorCB);
}

function validationcheck() {
}

function checkDuplicate(tx) {
	var fname = document.getElementById("ename").value;
	tx.executeSql("SELECT * FROM events WHERE eventName='"+ fname + "'",function(tx,results){
		if(results.rows.length>0)
			return false;
		else
			return true;
	}, errorCB);
}

function listItemClick(e) {
	window.localStorage.setItem("eventId", e);
	window.location.href = "details.html";
}

function create(){
	window.location.href = "create.html"
}

function list(){
	window.location.href = "list.html"
}

function getEventDetail(){
	db.transaction(queryEvent, errorCB);
}

function queryEvent(tx){
	var id=window.localStorage.getItem("eventId");
	tx.executeSql('SELECT * FROM events where id='+id, [], initEventData, errorCB);
}

function initEventData(tx, results){

	var ename = document.getElementById("ename");

	var date = document.getElementById("date");
	var time = document.getElementById("time");


	if(results.rows.length > 0)
	var row = results.rows.item(0);

	ename.innerHTML = row["eventName"]!=null?row["eventName"]:"";
	date.innerHTML = row["startDate"]!=null?row["createDate"]:"";
	time.innerHTML = row["dateOfEvent"]!=null?row["dateOfEvent"]:"";

	location.value = row["venueId"];
	org.value = row["organizer"];

	if(location != null){
		getLocation(location,tx);
	}

	if(org != null){
		getOrg(org,tx);
   	}
}

function getLocation(id,tx){
	tx.executeSql('SELECT name FROM organizers where id='+id, [], function(tx, results){
		if(results.rows.length > 0){
			var row = results.rows.item(0);
			var org = document.getElementById("org");
			org.innerHTML = row["name"];
		}
	}, errorCB);
}

function getOrg(id,tx){
	tx.executeSql('SELECT name FROM venue where id='+id, [], function(tx,results){
		if(results.rows.length > 0){
			var row = results.rows.item(0);
			var location = document.getElementById("location");
			location.innerHTML = row["name"];
		}
	}, errorCB);
}

function saveNew(){
	var tx = _seft.tx;
	var ename = document.getElementById("ename").value;
    var date = document.getElementById("date").value;
    var time = document.getElementById("time").value;
    var org = document.getElementById("org").value;
    var location = document.getElementById("location").value;

    var isValid = validate(ename,date,location,tx);
    if(isValid.length>0)
    	alert(isValid);
    else{
    	if(org!=null)
    		org = getOrgByName(org,tx);
    	if(location!=null)
    		location = getLocationByName(location,tx);
    	save(name,date,time,location,org,tx);
    }


}

function save(name,date,time,location,org,tx){
	tx.executeSql("insert into events (eventName,startDate,dateOfEvent,venueId,organizer) value("+ename+","+date+","+time+","+venueId+","+org+")",[], null,errorCB);
}

function validate(ename,date,location,tx){
	var valid = "";
	if(ename.length<=0)
		valid += "Please enter event name! \n";
//	else if(!checkDuplicate(tx))
//    	valid += "Duplicate event";
	if(date.length<=0)
		valid += "Please enter date of event! \n";
	if(location.length<=0)
		valid += "Please enter location! \n";
	return valid;
}

function cancelCreate(){
	window.location.href = "list.html"
}

function getLocationByName(name,tx){
	tx.executeSql('SELECT id FROM venue where name='+name+"'", [], function(tx,results){
		if(results.rows.length > 0){
			var row = results.rows.item(0);
            return row["id"];
		}else{
			return createLocation(name,tx);
		}
	}, errorCB);
}

function getOrgByName(name,tx){
	tx.executeSql('SELECT id FROM organizers where name='+name+"'", [], function(tx,results){
		if(results.rows.length > 0){
			var row = results.rows.item(0);
			return row["id"];
		}else{
         	return createOrg(name,tx);
		}
	}, errorCB);
}

function createLocation(name,tx){
	tx.executeSql("Insert into venue (name) value('"+name+"')", function(tx,results){
		if(results.rows.length > 0){
			return results.insertId;
		}
	},errorCB);
}

function createOrg(name,tx){
	tx.executeSql("Insert into organizers (name) value('"+name+"')", function(tx,results){
		if(results.rows.length > 0){
			return results.insertId;
		}
	}, errorCB);
}

