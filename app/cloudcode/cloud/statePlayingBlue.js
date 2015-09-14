function StatePlayingBlue (eventAndRequest){
    this.checkin = function(){
        return "Noop:StatePlayingBlueCheckin";
    }
    this.checkout = function(eventAndRequest){
        return "Noop:StatePlayingBlueCheckout";
    }
    this.startGame = function(eventAndRequest){
            return "Arrrrrr... you cannot start already starten goims, savvy?";
    }

    this.stopGame = function(eventAndRequest){
        var _event = eventAndRequest.passedEvent;
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


        _event.set("state", "grey");
        _event.set("isEnded", true);
        var facility = _event.get("facility");
        facility.remove("events", {"__type":"Pointer","className":"Event","objectId":_event.id});
        facility.save();

        var eventPromise = Parse.Promise.as(_event);
        var eventPromised = Parse.Promise.when(eventPromise).then(function(_event){
            _event.save().then(function(object){
                return Parse.Promise.as(_event);
            });



        });

        var eventId = _event.get("objectId");
        var data = {channels: [eventId], data:{alert:"Stopped game"}, where: new Parse.Query(Parse.Installation)};
        Parse.Push.send({
                channels: ["CHN_"+_event.id],
                data:{
                        alert:"Stopping event/game"+_event.id,
                        event: {"id": _event.id, "participants": oldParticipants }
                    }
                },

                {
                success: function(bla){
                },
                error: function(e){
                }
            });
        return eventPromised;
    }
    this.setState = function(){
    }

}
module.exports = StatePlayingBlue;