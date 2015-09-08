function StateReadyLightblue () {
    this.checkin = function(eventAndRequest) {
        return "StateReadyLightblueCheckin does not allow checkins. Fukk off, will ya?";
    }
    this.checkout = function(eventAndRequest) {
        var _event = eventAndRequest.passedEvent;
        var _request = eventAndRequest.passedRequest;
        _event.remove("participants", {"__type":"Pointer","className":"Participation","objectId":_request.params.user.id} );
        if (_event.get("participants").length > 0){
            _event.set("state", "orange");
        }
        if (_event.get("participants").length == 0){
            _event.set("state", "grey");
        }
        Parse.Push.send({
                    channels: [_event.id],
                    data:{
                            alert:"Checkout for:"+_event.id,
                            event: {"id": _event.id }
                        }
                    },

                    {
                    success: function(bla){

                    },
                    error: function(e){
                    }
                });
        return _event.save();
    }
    this.setState = function(){
    }
    this.startGame = function(eventAndRequest) {
        var _event = eventAndRequest.passedEvent;
        var _request = eventAndRequest.passedRequest;
        if (_event.get("participants").length >= _event.get("facility").get("sport").get("minPlayers")){
            _event.set("state", "blue");
            _event.set("startTime", new Date());
        }

        Parse.Push.send({
            channels: [_event.id],
            data:{
                    alert:"Starting event/game:"+_event.id,
                    event: {"id": _event.id, "participants": [] }
                }
            },

            {
            success: function(bla){
            },
            error: function(e){
            }
        });

        return _event.save();

    }

}
module.exports = StateReadyLightblue;