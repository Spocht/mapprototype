

var state;




/*var openingState = function (){

}*/

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


module.exports = State;

/*module.exports = {
    initialize: function (){
        //state = function() {
        var s = {};
            s.prototype = {
                checkin: function(context) {
                    return "prototype checkin";
                }
            },
            //s.prototype.checkin = function(context){
//
  //          };

            s.getOpeningState = function() {
                return openingState;
            };


            s.setState =  function(state) {
                this.state = state;
            }
        //};

        return {
            state : s,
            getOpeningState : s.getOpeningState,
            checkin : function (){
                return s.checkin;
            }
        };
    }

    //prototype.checkin: function(context) {
    //    return "Abstract checkin";
    //}*

}*/

