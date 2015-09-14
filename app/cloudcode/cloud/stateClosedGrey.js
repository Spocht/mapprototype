function StateClosedGrey (){
    var stateOpenOrange = require("cloud/stateOpenOrange");
    var stateOpenOrangeInstance = new stateOpenOrange;

    //same functionality here as in orange.
    this.checkin = stateOpenOrangeInstance.checkin;
    this.checkout = function(eventAndRequest){
        return Parse.Promise.as("Elvis cannout checkout from an empty building thus he already left.");
    }

    this.stopGame = function(eventAndRequest){
        return Parse.Promise.as("Stopping a closed game does not make Elvis happy");

    }
    this.setState = function(){

    }
}

module.exports = StateClosedGrey;

