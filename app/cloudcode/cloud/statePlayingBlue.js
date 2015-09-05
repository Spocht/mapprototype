function StatePlayingBlue (){
    this.checkin = function(){
        return "StatePlayingBlueCheckin";
    }
    this.checkout = function(){
        return "StatePlayingBlueCheckout";
    }
    this.setState = function(){
    }

}
module.exports = StatePlayingBlue;