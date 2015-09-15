
var state = require("cloud/state.js");
var stateInstance = new state;



var job = require("cloud/job.js");
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
			var eventState = stateInstance.getStateOfEvent(thatEventAndRequest);
			stateInstance.setState(eventState);
			var resp = stateInstance.checkin(thatEventAndRequest).then(function(object){
			    return response.success("SUCCESS");
			},function(object){
			    return response.error("FAILED");
			});
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
        	stateInstance.checkout(thatEventAndRequest).then(function(object){
        	    return response.success("SUCCESS");
        	});
		}, error: function(error) {
			response.error("FAILED");
		}
	});

	//how to manuall overwrite a prototype-function
	stateInstance.checkout = function(context){
		return "yeah";
	}

});



Parse.Cloud.define("startGame", function(request, response){
var eventQuery = new Parse.Query("Event");
	eventQuery.equalTo("objectId", request.params.event.id).include("facility.sport");
	eventQuery.first({
		success : function(event) {
			var thatEventAndRequest = {passedEvent:event, passedRequest:request};
			var eventState = stateInstance.getStateOfEvent(thatEventAndRequest);
			stateInstance.setState(eventState);

			stateInstance.startGame(thatEventAndRequest).then(function(object){
			    response.success("SUCCESS");
			});
		},
		error : function(error) {
			response.error("FAILED");
		}
	});
});

Parse.Cloud.define("stopGame", function(request, response){
    var eventQuery = new Parse.Query("Event");
	eventQuery.equalTo("objectId", request.params.event.id).include("facility.sport");
	eventQuery.first({
		success : function(event) {
			var thatEventAndRequest = {passedEvent:event, passedRequest:request};
			var eventState = stateInstance.getStateOfEvent(thatEventAndRequest);
			stateInstance.setState(eventState);

			stateInstance.stopGame(thatEventAndRequest).then(function(object){
			    response.success("SUCCESS");
			});
		},
		error : function(error) {
			response.error("FAILED");
		}
	});
});

function participants(eventAndRequest) {


        Parse.Cloud.useMasterKey();
            var event = eventAndRequest.passedEvent;
            var request = eventAndRequest.passedRequest;

            var participantsCount = 0;
            var retIds = [];
            var retIdsObject = function(){
                var values = [];
                function add(v) {
                    return function(v){
                        console.log("Pushing into values");
                        console.log(v);
                        values.push(v);
                        console.log("values.length: ");
                        console.log(values.length);
                        console.log("values.contents: ");
                        console.log(values);
                        return v;
                    }(v);

                };
                function get(){
                    return values;
                };
                return {
                    add:add,
                    get:get
                };

            }();

            var gatherIds = function(event){
                var promise = new Parse.Promise();
                try {
                    participantsCount = event.get("participants").length;
                } catch (e) {
                    //well... it's just empty.
                }

                function getIds(){
                    return retIds;
                }
                //callback for foreach
                var ids = {
                    retrieveIds : function(elem) {
                        //var id = elem.id;
                        console.log("Participant id: ");
                        console.log(elem.id);
                        retIdsObject.add(elem.id);
                        console.log("After adding in retIdsObject ");
                        console.log(retIdsObject.get());
                    },
                    getIds : function(){
                        return retIds;
                    }
                };



                if (participantsCount > 0){
                    var localCopyEvent = event;
                    localCopyEvent.get("participants").forEach(function(value)
                    {
                        ids.retrieveIds(value);
                        retIds.push(value.id);
                        console.log("EACHED");
                        console.log(retIds);
                    }
                    );
                    console.log("Resolved");
                    console.log(retIds);
                    promise.resolve();
                }
                else {
                    promise.reject();
                }
                console.log("gatherIDS");
                return promise;
            };




            var gatherThemAll = gatherIds(event);
            var test = Parse.Promise.when(gatherThemAll).then(function(p){
                    console.log(arguments);
                }, function(){
                    console.log(arguments);
                }
            ).then(function(p){
                return retIdsObject.get();
            });


            console.log("IDS");
            console.log(retIdsObject.get());
            //response.success(retIds);


            var bla = gatherIds(event).then(function(somestuff){
                var p = new Parse.Promise();
                console.log("somestuff");
                console.log(somestuff);
                p.resolve(somestuff);
                //return p;

            }).then(function(priorResult){

                if (retIds.length > 0){
                    var promise = new Parse.Promise();
                    var participation = Parse.Object.extend("Participation");
                    var participationInstance = new participation;
                    var user = Parse.Object.extend("SpochtUser");
                    var userInstance = new user;
                    userInstance.id = request.params.user.id;
                    participationInstance.set("user", userInstance);
                    participationInstance.save().then(function(p) {
                        promise.resolve(participationInstance);
                        return p;
                    });
                    return promise;
                } else {
                    var promise = new Parse.Promise();
                    var participation = Parse.Object.extend("Participation");
                    var pq = Parse.Object.extend("Participation");
                    var participationsQuery = new Parse.Query(pq);
                    return participationsQuery.containedIn("objectId", retIds).find().then(function(results){
                        var promise2 = new Parse.Promise();
                        var resolved = false;
                        var retObject;
                        results.forEach(function(r){

                            if (r) {
                                console.log("R");
                                console.log(r);

                            } else {
                                promise2.reject();
                            }
                        });
                        promise2.resolve();


                        //return promise.resolve("fett");
                        //return object;
                        return promise2;
                    }, function(error){
                        console.log("Error");
                        console.log(error);
                    });
                }


                //var checkParticipants = participants(thatEventAndRequest);

            }).then(function(result){
                //var promise = new Parse.Promise();
                //return promise.resolve(result);
            });


//
//        if (!alreadyInThere) {
//            _event.addUnique(
//            "participants",
//                {"__type":"Pointer",
//                "className":"Participation",
//                "objectId":request.params.user.id}
//            );
//            participantsCount++;
//        }
//
//        if (participantsCount >= _event.get("facility").get("sport").get("minPlayers")) {
//            _event.set("state", "lightblue");
//        }

        var eventPromise = Parse.Promise.as(event);
        var pushPromise = Parse.Promise.as(event);
        //https://parse.com/docs/js/api/symbols/Parse.Promise.html#.as
        //with a little hacking from me
        var eventPromised = Parse.Promise.when(eventPromise).then(function(_event){
            _event.save().then(function(object){

             });

             return _event;

        })._result;


//                var eventId = event.get("objectId");
//                var data = {channels: [eventId], data:{alert:"Checked in"}, where: new Parse.Query(Parse.Installation)};
//
//                Parse.Push.send({
//                        channels: [event.id],
//                        data:{
//                                alert:"Checked in"+event.id,
//                                event: {"id": event.id, "participants": [] }
//                            }
//                        },
//
//                        {
//                        success: function(bla){
//                            // Push was successful
//                                        response.success(bla);
//                        },
//                        error: function(e){
//                            response.error(error);
//                        }
//                    });
        return eventPromised;
    }









