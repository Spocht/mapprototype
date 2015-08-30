
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});


var state = require("cloud/state.js");
var stateInstance = new state;

var stateOpenOrange = require("cloud/stateOpenOrange.js");
var stateOpenOrangeInstance = new stateOpenOrange;



Parse.Cloud.define("spochtRokks", function(request, response) {
	var query = new Parse.Query("MyUser");
	query.equalTo("firstname", request.params.firstname);
	query.find({
		success: function(results) {
			response.success(results);
			//response.success(request.params.firstname);
		},
		error: function() {
			response.error("nope");
		}
	});
	
});


//curl -X POST  -H "X-Parse-Application-Id: $ID"  -H "X-Parse-REST-API-Key: $KEY"  -H "Content-Type: application/json"  -d '{"event":{"id" : "8vK94WXVGe"}}'  https://api.parse.com/1/functions/checkin

Parse.Cloud.define("checkin", function(request, response) {
	var query = new Parse.Query("Event");
	//response.success(request.params.event.id);
	query.get(request.params.event.id, {
		success : function(event) {
			response.success(event);
		},
		error : function(error) {
			response.error(error);
		}
	});

});

//Log out a user from an ongoing event in case they get too far away.
//Test curl:
//curl -X POST  -H "X-Parse-Application-Id: $XPARSEAPPLICATIONID"  -H "X-Parse-REST-API-Key: $RESTAPIKEY"  -H "Content-Type: application/json"  -d '{"firstname":"Strumpen"}'  https://api.parse.com/1/functions/checkout

Parse.Cloud.define("checkout", function(request, response) {
	var query = new Parse.Query("");

	var retVal = stateInstance.checkout(this);
	stateInstance.checkout = function(context){
		return "yeah";
	}
	//state.checkin(this);
	retVal += stateInstance.checkout(this);
	stateInstance.checkin = stateInstance.getOpeningState();
	stateInstance.setState(stateOpenOrangeInstance);
	retVal = retVal+stateInstance.checkout(this);
	response.success(retVal);

});
