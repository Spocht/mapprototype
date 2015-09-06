function StateOpenOrange (){


    var that = {};


    this.checkin = function(eventAndRequest){



        /*var query = new Parse.Query("Drivers");
          query.equalTo("user", {
                  __type: "Pointer",
                  className: "_User",
                  objectId: userID
              });

          query.find({*/


        //yeah... please understand that i was temporarily increative
        //in regard to naming variables.
        var _event = eventAndRequest.passedEvent;
        var request = eventAndRequest.passedRequest;


        var that = this;

        var p;
        var participants;
        var participantsCount = 0;
        try {
            //participantsCount = _event.get("participants").length;
            participantsCount = _event.get("participants").length;

        }
        catch (e) {
            //NoOp! tablecolumn might be empty.
        }

        //event.addUnique is special, as it atomically only adds uniquely in the
        //backend. the object contains duplicates when one adds a user which is already
        //in the collection. thus, counting gets wonked and manual counting labor is
        //deemed necessary.
        var alreadyInThere = false;
        if (participantsCount > 0) {
            for(var i = 0; i< _event.get("participants").length; i++) {
                console.log(_event.get("participants")[i]);
                p = _event.get("participants")[i];
                if (p.id == request.params.user.id) {
                    alreadyInThere = true;
                }
            }
        }
        if (!alreadyInThere) {
            _event.addUnique(
            "participants",
                {"__type":"Pointer",
                "className":"Participation",
                "objectId":request.params.user.id}
            );
            participantsCount++;
        }

        if (participantsCount >= _event.get("facility").get("sport").get("minPlayers")) {
            _event.set("state", "lightblue");
        }

        var eventPromise = Parse.Promise.as(_event);
        var pushPromise = Parse.Promise.as(_event);
        //https://parse.com/docs/js/api/symbols/Parse.Promise.html#.as
        //with a little hacking from me
        var eventPromised = Parse.Promise.when(eventPromise).then(function(_event){
            _event.save().then(function(object){

             });

             return _event;

        })._result;


        var eventId = _event.get("objectId");
        var data = {channels: [eventId], data:{alert:"Checked in"}, where: new Parse.Query(Parse.Installation)};
        Parse.Push.send({
            	channels: ["VRjWyksupm"],
            	data:{
            			alert:"Checked in really"
            		}
            	},

            	{
            	success: function(bla){
        			// Push was successful
                                response.success(bla);
            	},
            	error: function(e){
            		response.error(error);
            	}
            });
        console.log("data");
        console.log(data);
        console.log("event");
        console.log(_event);

        return eventPromised;

    }
    this.checkout = function(eventAndRequest){
        var event = eventAndRequest.passedEvent;
        var request = eventAndRequest.passedRequest;
        var minPlayers = event.get("facility").get("sport").get("minPlayers");
        try {
            event.remove("participants", {"__type":"Pointer","className":"Participation","objectId":request.params.user.id} );
            if (event.get("participants").length == 0) {
                event.set("state", "yellow");
            }
        }
        catch (error) {
            console.log("Participants was empty");
        }


        event.save();
        return "StateOpenOrangeCheckout:"+minPlayers+""+event.get("participants").length;
    }
    this.setState = function(){

    }

}

module.exports = StateOpenOrange;

