function StatePlayingBlue (eventAndRequest){
    var that = this;

    var expirationSeconds = 300;

    this.checkin = function(){
        return "Noop:StatePlayingBlueCheckin";
    }
    this.checkout = function(eventAndRequest){
        eventAndRequest.passedRequest.params.outcome = {"value":"GIVEUP"};
        return that.stopGame(eventAndRequest);

    }
    this.startGame = function(eventAndRequest){
            return "Arrrrrr... you cannot start already starten goims, savvy?";
    }

    this.stopGame = function(eventAndRequestAndOutcome){
        var _event = eventAndRequestAndOutcome.passedEvent;
        var _request = eventAndRequestAndOutcome.passedRequest;
        var _outcome = eventAndRequestAndOutcome.passedRequest.params.outcome.value;

        Parse.Cloud.useMasterKey();

        //participants in this event need to be deleted...
        //question is: when? the pushmessage needs to send the old participants
        //for further processing in the PushReceiver.
        //by the time
        //the client is returned the event, client has no more knowledge
        //of removed participants.
        //so let's capture them little buggaz here with a little IIFE...
        //and pass them as params for push.
        var oldParticipants = function(e){
                   return e.get("participants");
                }(_event);


        var promises = [];



        _event.set("state", "grey");
        _event.set("isEnded", true);
        var facility = _event.get("facility");
        facility.remove("events", {"__type":"Pointer","className":"Event","objectId":_event.id});



        promises.push(facility.save());
        promises.push(_event.save());


        function updateParticipations(){

            var otherOutcome = function(outcome){
            var ret = "";
                if (outcome == "WIN") {
                    ret = "LOSE";
                }
                if (outcome == "LOSE" || outcome == "GIVEUP") {
                    ret =  "WIN";
                }
                if (outcome == "TIE"){
                    ret = "TIE";
                }
                return ret;
            };

            var calculateXP = function(outcome) {
                var ret = 0;
                if (outcome == "WIN") {
                    ret = 6;
                }
                if (outcome == "LOSE") {
                    ret = 3;
                }
                if (outcome == "GIVEUP") {
                    ret = 1;
                }
                if (outcome == "TIE"){
                    ret = 4;
                }
                return ret;
            };




            function userQueryFunc(id, _outcome) {
                var userQuery = new Parse.Query("SpochtUser");
                userQuery.equalTo("objectId", id).include("experience.pointer");
                userQuery.get().then(function(object){
                    var experience = Parse.Object.extend("Experience");
                    var xpInstance = new experience;
                    var promises = [];
                    object.get("experience").forEach(function(value){
                        value.increment("xp", _outcome);
                        promises.push(value.save());
                    });
                    return Parse.Promise.when(promises).then(function(object){
                        return Parse.Promise.as();
                    });

                });
                return Parse.Promise.as();
            }



            var participantPromises = new Parse.Promise();
            _event.get("participants").forEach(function(result){
                var participation = Parse.Object.extend("Participation");
                var participationInstance = new participation;
                participationInstance.id = result.id;
                participationInstance.fetch().then(function(object) {
                    return Parse.Promise.as().then(function(object){




                    }).then(function(object){
                        if (participationInstance.get("user").id == _request.params.user.id) {
                            participationInstance.set("outcome", _outcome);
                            var myOutcome = calculateXP(_outcome);
                            return userQueryFunc(participationInstance.get("user").id, myOutcome);



                        }
                        else {
                            participationInstance.set("outcome", otherOutcome(_outcome));
                            var myOtherOutcome = calculateXP(otherOutcome(_outcome));
                            return userQueryFunc(participationInstance.get("user").id, myOtherOutcome);
                        }
                    });

                }).then(function(object){
                    promises.push(participationInstance.save());
                });
            });
            Parse.Promise.when(participantPromises).then(function(object){
                return Parse.Promise.as();
            });
        }
        promises.push(updateParticipations());

        return Parse.Promise.when(promises).then(function (object){
            var promise = new Parse.Promise();
            Parse.Push.send({
                expiration_interval: expirationSeconds,
                channels: ["CHN_"+_event.id],
                data:{
                        alert:"Stopping event/game"+_event.id,
                        event: {"id": _event.id, "participants": oldParticipants }
                    }
                },

                {
                success: function(bla){
                    promise.resolve(bla);
                },
                error: function(e){
                    promise.error(e);
                }
            });
            return promise;
        });

        function addXP() {

        }

    }
    this.setState = function(){
    }

}
module.exports = StatePlayingBlue;