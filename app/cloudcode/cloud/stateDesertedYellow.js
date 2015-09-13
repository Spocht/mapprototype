function StateDesertedYellow (){

    this.checkin = function(eventAndRequest){
        return Parse.Promise.as("No checking in from status yellow");
    }
    this.checkout = function(eventAndRequest){
        return Parse.Promise.as("Elvis cannout checkout from an empty building thus he already left.");
    }
    
    this.setState = function(){
    }
}

module.exports = StateDesertedYellow;

