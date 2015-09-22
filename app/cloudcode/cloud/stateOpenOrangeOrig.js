

function StateOpenOrange (){


    var that = {};
    this.checkin = function(eventAndRequest){


        Parse.Cloud.useMasterKey();
        var _event = eventAndRequest.passedEvent;
        var request = eventAndRequest.passedRequest;

        var participantsCount = 0;
        var retIds = [];
        try {
            participantsCount = _event.get("participants").length;
        }
        catch (e) {

            participantsCount++;
            retIds.push(makeNewParticipation(request));
        }

        var ids = {
            retrieveIds : function(elem) {
                var id = elem.id;
                console.log("P:");
                console.log(id);
                //this.push(id);
                retIds.push(id);
            }
        };
        var counter = 0;


        //process participants into an array
        if (participantsCount > 0){
            var localCopyEvent = _event;
            localCopyEvent.get("participants").forEach(function(value)
            {
                ids.retrieveIds(value)
            },
                retIds
            );
        }


        var promisedParticipations = new Parse.Promise();
        var pq = Parse.Object.extend("Participation");
        var participationsQuery = new Parse.Query(pq);
        var county = pq.count().then(function(object){
            console.log("Number");
            console.log(number);
        }, function(error){
            console.log("Error");
            console.log(error);
        });


        var promisess = [];
        promisess.push(county);

        Parse.Promise.when(promisess).then(function(){
                console.log(arguments);
            }, function(){
                console.log(arguments);
            }
        );







        participationsQuery.find().then(function(object){
             var promise = Parse.Promise.as();
             _.each(results, function(result)
             {
                console.log("Found");
                console.log(object);
                alreadyInThere = true;

             });
             return promise;
             },
             function(error) {
                   console.log("!Found");
                   console.log(error);
             }


        );




        //event.addUnique is special, as it atomically only adds uniquely in the
        //backend. the local object contains duplicates when one adds a user which is already
        //in the collection. thus, counting gets wonked and manual counting labor is
        //deemed necessary.
        //this is actually a corner case for when checkin in state orange is called
        //twice with the same event and user.id. but that case is actually handled
        //on the device. still, i leave it here.

        var alreadyInThere = false;
        if (participantsCount > 0) {
            for(var i = 0; i< _event.get("participants").length; i++) {
                console.log(_event.get("participants")[i]);
                p = _event.get("participants")[i];
                if (p.id == participation.id) {
                    alreadyInThere = true;
                }
            }
        }


        if (!alreadyInThere) {
            _event.addUnique(
            "participants",
                {"__type":"Pointer",
                "className":"Participation",
                "objectId":request.params.user.id}
            );
            participantsCount++;
        }

        if (participantsCount >= _event.get("facility").get("sport").get("minPlayers")) {
            _event.set("state", "lightblue");
        }

        var eventPromise = Parse.Promise.as(_event);
        var pushPromise = Parse.Promise.as(_event);
        //https://parse.com/docs/js/api/symbols/Parse.Promise.html#.as
        //with a little hacking from me
        var eventPromised = Parse.Promise.when(eventPromise).then(function(_event){
            _event.save().then(function(object){

             });

             return _event;

        })._result;


        var eventId = _event.get("objectId");
        var data = {channels: [eventId], data:{alert:"Checked in"}, where: new Parse.Query(Parse.Installation)};

        Parse.Push.send({
            	channels: [_event.id],
            	data:{
            			alert:"Checked in"+_event.id,
            			event: {"id": _event.id, "participants": [] }
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
        return eventPromised;

    }
    this.checkout = function(eventAndRequest){
        var event = eventAndRequest.passedEvent;
        var request = eventAndRequest.passedRequest;
        var minPlayers = event.get("facility").get("sport").get("minPlayers");
        try {
            event.remove("participants", {"__type":"Pointer","className":"Participation","objectId":request.params.user.id} );
            if (event.get("participants").length == 0) {
                event.set("state", "yellow");
            }
        }
        catch (error) {
            console.log("Participants was empty");
        }
        Parse.Push.send({
            channels: [""],
            data:{
                    alert:"Checkout for:"+event.id,
                    event: {"id": event.id }
                }
            },

            {
            success: function(bla){
                // Push was successful
                //response.success(bla);
            },
            error: function(e){
                //response.error(error);
            }
        });

        event.save();
        return "StateOpenOrangeCheckout:"+minPlayers+""+event.get("participants").length;
    }
    this.setState = function(){

    }
    this.stopGame = function(){
        return "NoOp: StateOpenOrangeStartGame";
    }
    this.stopGame = function(){
        return "NoOp: StateOpenOrangeStopGame";
    }

    var isAlreadyParticipant = function(_event, _request) {
        var alreadyInThere = false;
        var participantsLength = 0;
        var _e1 = _event;
        try {
            participantsLength = _event.get("participants").length;
        }
        catch (e) {
            return alreadyInThere;
        }

        var gay = [];
        var participationsQuery = new Parse.Query("Participation");
        var onlyObjectIds = fillObjectIdsFromParticipants(_event, gay);



        console.log("TF");
        console.log(onlyObjectIds);
        console.log("Gay");
        console.log(gay);
        participationsQuery.containedIn("objectId", ['d8QfOZyU07']);

        var findPromise = Parse.Promise.as(participationsQuery);


        return Parse.Promise.when(findPromise).then(function(_event){
           participationsQuery.find({
               success: function(results){
                   h.o();
                   alreadyInThere = true;
               },
               error: function(e){
                   h.i();
               }
           }).then(function(result){
               return result;
           });

        })._result;
            //console.log(_event.get("participants")[i]);
            //return _event.get("participants")[i].id;
            //var participationObject = Parse.Object.extend("Participation");
            //var participationInstance = new participationObject();
            //participationInstance.id = _event.get("participants")[i].id;
            //participationsQuery.get(participationInstance.id);
            //return participationsQuery.containedIn("objectId", _event.get("participants"));
            //var participations;
            //var id = function(e) {
            //    return e.id;
            //}(_event.get("participants")[i]);



            //participationsQuery.equalTo("objectId", id);




    //            if (participation.id == _event.get("participants")[i]) {
    //                alreadyInThere = true;
    //                break;
    //            }




        return alreadyInThere;
    }


    var makeNewParticipation = function(request){
        var promises = [];
        var participation = Parse.Object.extend("Participation");
        var participationInstance = new participation;
        var user = Parse.Object.extend("SpochtUser");
        var userInstance = new user;
        userInstance.id = request.params.user.id;
        promises.push(participationInstance.set("user", userInstance));
        promises.push(participationInstance.save());


        //promises.push(participationInstance.save());

        Parse.Promise.when(promises).then(function(){
                console.log(arguments);
            }, function(){
                console.log(arguments);
            }
        );
        return participationInstance.id;
    }

    var fillObjectIdsFromParticipants = function(event, targetArray) {

        var ids = [];


        var finished = false;
        var eventObject = Parse.Object.extend("Participant");
        var eventInstance = new eventObject();
        eventInstance.id = event.id;
        //return event;

        var localEvent = event;

        var length = event.get("participants").length;
        var counter = 0;




        //console.log("IDS");
        //console.log(ids);



        function returner (arr) {
            var b = arr;
            console.log("IDS");
            console.log(b);
            return b;
        }
    }
}


module.exports = StateOpenOrange;

