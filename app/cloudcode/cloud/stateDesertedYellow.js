function StateDesertedYellow (){


    var that = {};

    this.checkin = function(eventAndRequest){



        /*var query = new Parse.Query("Drivers");
          query.equalTo("user", {
                  __type: "Pointer",
                  className: "_User",
                  objectId: userID
              });

          query.find({*/




        //this.prototype.checkin.call(eventAndRequest);
        var _event = eventAndRequest.passedEvent;
        var request = eventAndRequest.passedRequest;


        var that = this;

        //var participantsCount = _event.get("participants").length;
        _event.addUnique("participants", {"__type":"Pointer","className":"Participation","objectId":request.params.user.id} );
        _event.set("state", "orange");

        var eventPromise = Parse.Promise.as(_event);

        //https://parse.com/docs/js/api/symbols/Parse.Promise.html#.as
        //with a little hacking from me
        return Parse.Promise.when(eventPromise).then(function(_event){
            _event.save().then(function(object){
                  console.log(event.get("participants").length);
                  //that = "hello";
                  //event.that = that;
             });
             //return _event;
             return _event;

        })._result;

    }
    this.checkout = function(eventAndRequest){
            return "StateDesertedYellowCheckout:NoOp";
        }
    this.setState = function(){

    }

}

module.exports = StateDesertedYellow;

