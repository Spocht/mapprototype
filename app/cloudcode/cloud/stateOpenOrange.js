

function StateOpenOrange (){
    var that = {};
    this.checkin = function(eventAndRequest){


        Parse.Cloud.useMasterKey();
        var event = eventAndRequest.passedEvent;
        var request = eventAndRequest.passedRequest;




        //var idsPromise = Parse.Promise.as();
        var ids = [];
        var idsLength = 0;
        var createdParticipant;
        function getIds(){
            p = new Parse.Promise();
            try {
                event.get("participants").forEach(function(value){
                    ids.push(value.id);
                });
            }
            catch (e){
                //event.get("participants") mighta been empty.
            }
            idsLength = ids.length;
            return Parse.Promise.as();

        }

        function setParticipant(){
            var p2 = new Parse.Promise();
            return Parse.Promise.as(ids);
        }



        function resolution(){
            return new Parse.Promise.as({ids:ids,p:createdParticipant});
        }
        //after this chain is resolved, the ids array is accessible
        //in this function.
        //might use the results... so this serves as a reference.
        //http://stackoverflow.com/questions/25254355/how-to-pass-extra-data-down-a-parse-promise-chain



        //return ids;

        function createParticipationIfNecessary(){
            var p3 = new Parse.Promise();
            var participation = Parse.Object.extend("Participation");
            var participationInstance = new participation;
            var user = Parse.Object.extend("SpochtUser");
            var userInstance = new user;
            var e = Parse.Object.extend("Event");
            var eventInstance = new e;
            userInstance.id = request.params.user.id;
            participationInstance.set("user", userInstance);
            eventInstance.id = event.id;
            participationInstance.set("event", eventInstance);
            var retArray = [];
            if (idsLength == 0){
                participationInstance.save().then(function(object){
                    ids.push(object.id);
                    retArray.push(object.id);
                    p3.resolve(retArray);
                });
            }
            else {
                var promise = new Parse.Promise();
                var participation = Parse.Object.extend("Participation");
                var pq = Parse.Object.extend("Participation");
                var participationsQuery = new Parse.Query(pq);
                participationsQuery.equalTo("event", {"__type":"Pointer",
                      "className":"Event",
                      "objectId":event.id}
                );
                participationsQuery.find().then(function(results){


                    var promises = [];
                    results.forEach(function(value){
                        if (value.get("user").id != userInstance.id) {
                            var p =
                                participationInstance.save().then(function(object){
                                    ids.push(object.id);
                                    retArray.push(object.id);
                                    return Parse.Promise.as(object);
                                });
                            promises.push(p);

                        }
                    });
                    Parse.Promise.when(promises).then(function(object){
                        p3.resolve(retArray);
                    });


                });
            }
            return p3;
        }


        function addParticipant(){
            var p = new Parse.Promise();
            var promises = [];
            ids.forEach(function(value){
                event.addUnique(
                    "participants",
                        {"__type":"Pointer",
                        "className":"Participation",
                        "objectId":value}
                );
                promises.push(event.save());

            });
            return Parse.Promise.when(promises);
        }

        function switchState(){
            if (ids.length >= event.get("facility").get("sport").get("minPlayers")) {
                event.set("state", "lightblue");
            } else {
                event.set("state", "orange");
            }
            event.save().then(function(object){
                return Promise.as(object);
            });

        }

        function push(){
            Parse.Push.send({
                channels: ["CHN_"+event.id],
                data:{
                        alert:"Checked in"+event.id,
                        event: {"id": event.id, "participants": [] }
                    }
                },

                {
                success: function(bla){
                    // Push was successful
                     return Parse.Promise.as(ids);
                },
                error: function(e){
                    return Parse.Promise.error(bla);
                }
            });
        }

        //chain it all together
        return getIds()
        .then(setParticipant)
        .then(createParticipationIfNecessary)
        .then(addParticipant)
        .then(switchState)
        .then(push)
        .then(function(object){
            return object;
        });


    }
    this.checkout = function(eventAndRequest){
        var event = eventAndRequest.passedEvent;
        var request = eventAndRequest.passedRequest;
        var minPlayers = event.get("facility").get("sport").get("minPlayers");

        var pq = Parse.Object.extend("Participation");
        var participationsQuery = new Parse.Query(pq);
        participationsQuery.equalTo("event",
            {
                "__type":"Pointer",
                "className":"Event",
                "objectId":event.id
            }
        );

        participationsQuery.equalTo("user",
            {
               "__type":"Pointer",
               "className":"SpochtUser",
               "objectId":request.params.user.id
            }
        );

        return participationsQuery.first().then(function(result){

            var promises = [];
            event.remove("participants", {"__type":"Pointer","className":"Participation","objectId":result.id});
            promises.push(event.save());
            promises.push(result.destroy());

            Parse.Promise.when(promises).then(function(object){
                return Parse.Promise.as(event);
            });

        }).then(function(object){
            Parse.Push.send({
                channels: ["CHN_"+event.id],
                data:{
                        alert:"Checkout for:"+event.id,
                        event: {"id": event.id }
                    }
                },
                {
                success: function(bla){
                    return Parse.Promise.as(event);
                    // Push was successful
                    //response.success(bla);
                },
                error: function(e){
                    return Parse.Promise.error(event);
                    //response.error(error);
                }
            });
        });




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

    function newParticipation(request) {
            var p3 = new Parse.Promise();
            var participation = Parse.Object.extend("Participation");
            var participationInstance = new participation;
            var user = Parse.Object.extend("SpochtUser");
            var userInstance = new user;
            userInstance.id = request.params.user.id;
            participationInstance.set("user", userInstance);
            console.log("yeah");
            return participationInstance.save();
//            var promises = [];
//            promises.push(participationInstance.save());
//            Parse.Promise.when(promises).then(function(object){
//                console.log("at least");
//                createdParticipant = participationInstance;
//                return Parse.Promise.as("whats up");
//            }, function(error){
//                console.log(error);
//                return "OOPS";
//            });
            //participationInstance.save()
//            return participationInstance.save().then(function(result){
//                createdParticipant = participationInstance;
//                console.log("Resolved");
//                p.resolve(result);
//            }, function(error){
//                console.log(error);
//            });
//            return p;


    }

    var makeNewParticipation = function(request){
        var promises = [];
        var participation = Parse.Object.extend("Participation");
        var participationInstance = new participation;
        var user = Parse.Object.extend("SpochtUser");
        var userInstance = new user;
        userInstance.id = request.params.user.id;
        promises.push(participationInstance.set("user", userInstance));
        return promises.push(participationInstance.save());
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

