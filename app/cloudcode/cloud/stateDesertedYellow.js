function StateDesertedYellow (){

    this.checkin = function(eventAndRequest){
        var _event = eventAndRequest.passedEvent;
        var request = eventAndRequest.passedRequest;
        //var that = this;
        _event.addUnique("participants", {"__type":"Pointer","className":"Participation","objectId":request.params.user.id} );
        _event.set("state", "orange");
        var eventPromise = Parse.Promise.as(_event);

       var eventPromised =  Parse.Promise.when(eventPromise).then(function(_event){
            _event.save().then(function(object){
             });
             return _event;
        })._result;

        Parse.Push.send(
            {
                channels: [_event.id],
                data:{
                        alert:"Checked in from deserted state"+_event.id,
                        event: {"id": _event.id, "participants": []}
                    }
                },
                {
                success: function(bla){
                },
                error: function(e){
                    response.error(error);
                }
            }
        );

        return eventPromised;
    }
    this.checkout = function(eventAndRequest){
            return "Elvis cannout checkout from an empty building thus he already left.";
        }
    this.setState = function(){
    }
}

module.exports = StateDesertedYellow;

