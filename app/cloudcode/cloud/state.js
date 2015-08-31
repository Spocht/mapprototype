

var state;


//      CLOSED("grey"),
//      OPEN("orange"),
        var stateOpenOrange = require("cloud/stateOpenOrange.js");
        var stateOpenOrangeInstance = new stateOpenOrange;
//      DESERTED("yellow"),
//      READY("lightblue"),
        var stateReadyLightblue = require("cloud/stateReadyLightblue.js");
        var stateReadyLightblueInstance = new stateReadyLightblue;
//      PLAYING("blue");

function State() {

}

State.prototype.checkin = function checkin(){
    return "checking in";
};

State.prototype.checkout = function checkin(){
    return "checking out";
};

State.prototype.getOpeningState = function (){
    return checkin =  function(){
        return "still opening";
    }
}

State.prototype.setState = function (state_in) {
    this.checkin = state_in.checkin;
    this.checkout = state_in.checkout;
}

State.prototype.getStateOfEvent = function (state) {
    console.log("State");
    console.log(state);
    var stateField = state.passedEvent.get("state");
    if (stateField === undefined) {
        var event = new Parse.Object("Event");
        event.id = state.passedEvent.id;
        event.set("state",  "orange");
        event.save();
        return stateOpenOrangeInstance;
    }
    if (stateField == "orange") {
        return stateOpenOrangeInstance;
    }
    if (stateField == "lightblue") {
        return stateReadyLightblueInstance;
    }

}

module.exports = State;