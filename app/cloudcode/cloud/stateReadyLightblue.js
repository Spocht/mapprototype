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
            _event.set("state", "deserted");
        }
        return _event.save();
        //return "StateReadyLightblueCheckout";
    }
    this.setState = function(){
    }
    this.startGame = function(eventAndRequest) {

    }

}
module.exports = StateReadyLightblue;