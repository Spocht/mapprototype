


//FORMAT für PushReceiver


//push nicht nur bei transitionen, sondern bei allen operationen, die Event verändern.
var state;


//      CLOSED("grey"),
        var stateClosedGrey = require("cloud/stateClosedGrey.js");
        var stateClosedGreyInstance = new stateClosedGrey;
//      OPEN("orange"),
        var stateOpenOrange = require("cloud/stateOpenOrange.js");
        var stateOpenOrangeInstance = new stateOpenOrange;
//      DESERTED("yellow"),
        var stateDesertedYellow = require("cloud/stateDesertedYellow.js");
        var stateDesertedYellowInstance = new stateDesertedYellow;
//      READY("lightblue"),
        var stateReadyLightblue = require("cloud/stateReadyLightblue.js");
        var stateReadyLightblueInstance = new stateReadyLightblue;
//      PLAYING("blue");
        var statePlayingBlue = require("cloud/statePlayingBlue.js");
        var statePlayingBlueInstance = new statePlayingBlue;

function State() {

}


State.prototype.checkin = function checkin(eventAndRequest) {
    return "wannabe checkin here, kind sir.";
};

State.prototype.checkout = function checkout(eventAndRequest) {
    return "wannabe checkout here, kind sir.";
};

State.prototype.startGame = function startGame(eventAndRequest) {
    return "wannabe startGame here, kind sir.";
}

State.prototype.stopGame = function stopGame(eventAndRequest) {
    return "wannabe stopGame here, kind sir.";
}

State.prototype.getOpeningState = function (){
    return checkin =  function(){
        return "still opening";
    }
}

State.prototype.setState = function (state_in) {
    this.checkin = state_in.checkin;
    this.checkout = state_in.checkout;
    this.startGame = state_in.startGame;
    this.stopGame = state_in.stopGame;
}

State.myPush = function (payload, data) {
	Parse.Push.send({
        	channels: [""],
        	data:{
        			alert:"Checked in"
        		}
        	},

        	{
        	    success: function(bla){
        	},
        	    error: function(e){
        	}
        });
}


State.prototype.getStateOfEvent = function (state) {
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
    if (stateField == "blue") {
        return statePlayingBlueInstance;
    }
    if (stateField == "yellow") {
        return stateDesertedYellowInstance;
    }
    if (stateField == "grey") {
        return stateClosedGreyInstance;
    }

}

module.exports = State;