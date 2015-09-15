function StatePlayingBlue (eventAndRequest){
    var that = this;

    var expirationSeconds = 300;

    this.checkin = function(){
        return "Noop:StatePlayingBlueCheckin";
    }
    this.checkout = function(eventAndRequest){
        var localEAR = eventAndRequest;
        localEAR.outcome = "LOSE";
        return that.stopGame(localEAR);

    }
    this.startGame = function(eventAndRequest){
            return "Arrrrrr... you cannot start already starten goims, savvy?";
    }

    this.stopGame = function(eventAndRequestAndOutcome){
        var _event = eventAndRequestAndOutcome.passedEvent;
        var _outcome = eventAndRequestAndOutcome.outcome;
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

            };
            var participantPromises = new Parse.Promise();
            _event.get("participants").forEach(function(result){
                var participation = Parse.Object.extend("Participation");
                var participationInstance = new participation;
                participationInstance.id = result.id;
                participationInstance.set("outcome", _outcome);
                promises.push(participationInstance.save());
                console.log(result);
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





        //var eventPromise = Parse.Promise.as(_event);
//        var eventPromised = Parse.Promise.when(eventPromise).then(function(_event){
//            _event.save().then(function(object){
//                return Parse.Promise.as(_event);
//            });
//
//        });

        //var eventId = _event.get("objectId");
        //var data = {channels: [eventId], data:{alert:"Stopped game"}, where: new Parse.Query(Parse.Installation)};

        //return eventPromised;
    }
    this.setState = function(){
    }

}
module.exports = StatePlayingBlue;