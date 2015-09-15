function StateDesertedYellow (){
    var stateOpenOrange = require("cloud/stateOpenOrange.js");
    var stateOpenOrangeInstance = new stateOpenOrange;
    this.checkin = stateOpenOrangeInstance.checkin;
    this.checkout = stateOpenOrangeInstance.checkout;
    
    this.setState = function(){
    }
}

module.exports = StateDesertedYellow;

