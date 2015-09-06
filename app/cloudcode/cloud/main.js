
var state = require("cloud/state.js");
var stateInstance = new state;
//---REF
//var stateOpenOrange = require("cloud/stateOpenOrange.js");
//var stateOpenOrangeInstance = new stateOpenOrange;



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




//Test curl:
//curl -X POST  -H "X-Parse-Application-Id: $ID"  -H "X-Parse-REST-API-Key: $KEY"  -H "Content-Type: application/json"  -d '{"event":{"id" : "8vK94WXVGe"}, "user":{"id" : "PGcfBOKlmE"}}'  https://api.parse.com/1/functions/checkin

Parse.Cloud.define("checkin", function(request, response) {
	var eventQuery = new Parse.Query("Event");
	eventQuery.equalTo("objectId", request.params.event.id).include("facility.sport");
	eventQuery.first({
		success : function(event) {
			var thatEventAndRequest = {passedEvent:event, passedRequest:request};

			//http://iswwwup.com/t/4e7c1e42b8b3/javascript-parse-com-js-update-row-with-pointer-column.html --- reference
			//var facility = new Parse.Object("Facility");
			//facility.id = event.get('associatedFacility').id;

			var eventState = stateInstance.getStateOfEvent(thatEventAndRequest);
			stateInstance.setState(eventState);

//we might use a save method with callbacks. only an inline --- reference.
//			facility.save(null, {
//				success: function(fac) {
//					response.success("saved");
//				},
//				error: function(fac, error) {
//					response.error("not saved");
//
//				}
//			});

			//possible accessors... only here as a --- reference
			//response.success(facility.get('sport').get('minPlayers'));
			//response.success(event.get('associatedFacility').id);
			//response.success(request.params.event.id);
			//response.success(event.get("associatedFacility").get("name"));

			response.success(stateInstance.checkin(thatEventAndRequest));
		},
		error : function(error) {
			response.error(error);
		}
	});
});



//Log out a user from an ongoing event in case they get too far away.
//Test curl:
//curl -X POST  -H "X-Parse-Application-Id: $ID"  -H "X-Parse-REST-API-Key: $KEY"  -H "Content-Type: application/json"  -d '{"event":{"id" : "8vK94WXVGe"}, "user":{"id" : "PGcfBOKlmE"}}'  https://api.parse.com/1/functions/checkout

Parse.Cloud.define("checkout", function(request, response) {
	var eventQuery = new Parse.Query("Event");
	eventQuery.equalTo("objectId", request.params.event.id).include("facility.sport");
	eventQuery.first({
		success: function(event) {
			var thatEventAndRequest = {passedEvent:event, passedRequest:request};
        	var eventState = stateInstance.getStateOfEvent(thatEventAndRequest);
        	stateInstance.setState(eventState);
        	//if participants is empty, then this call hangs.
        	response.success(stateInstance.checkout(thatEventAndRequest));
		}, error: function(error) {
			response.error(error);
		}
	});

	//how to manuall overwrite a prototype-function
	stateInstance.checkout = function(context){
		return "yeah";
	}

});



Parse.Cloud.define("posh", function(request, response){
	var data = {channels: [""], data:{alert:"Checked in"}, where: new Parse.Query(Parse.Installation)};

    Parse.Push.send({
    	channels: [""],
    	data:{
    			alert:"Checked in"
    		}
    	},

    	{
    	success: function(bla){
			// Push was successful
                        response.success(bla);
    	},
    	error: function(e){
    		response.error(error);
    	}
    });
            //where: new Parse.Query(Parse.Installation)}).then(function(stuff){
            //}));
});






