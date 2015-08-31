function StateOpenOrange (){

    this.checkin = function(eventAndRequest){
    var event = eventAndRequest.passedEvent;
    var request = eventAndRequest.passedRequest;
        event.addUnique("participants", request.params.user.id);
        event.save();
        return "StateOpenOrangeCheckin";
    }
    this.checkout = function(eventAndRequest){
        var event = eventAndRequest.passedEvent;
        var request = eventAndRequest.passedRequest;
        event.remove("participants", request.params.user.id);
        event.save();
        return "StateOpenOrangeCheckout";
    }
    this.setState = function(){

    }

}

module.exports = StateOpenOrange;

