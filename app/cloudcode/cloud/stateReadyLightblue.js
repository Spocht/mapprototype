function StateReadyLightblue () {

    var expirationSeconds = 300;
    this.checkin = function(eventAndRequest) {
        return "StateReadyLightblueCheckin does not allow checkins. Fukk off, will ya?";
    }


    this.checkout = function(eventAndRequest){
        var event = eventAndRequest.passedEvent;
        var request = eventAndRequest.passedRequest;
        var minPlayers = event.get("facility").get("sport").get("minPlayers");

        var pq = Parse.Object.extend("Participation");
        var participationsQuery = new Parse.Query(pq);
        participationsQuery.equalTo("event",
            {
                "__type":"Pointer",
                "className":"Event",
                "objectId":event.id
            }
        );

        participationsQuery.equalTo("user",
            {
               "__type":"Pointer",
               "className":"SpochtUser",
               "objectId":request.params.user.id
            }
        );

        return participationsQuery.first().then(function(result){

            var promises = [];
            event.remove("participants", {"__type":"Pointer","className":"Participation","objectId":result.id});
            event.set("state", "orange");
            promises.push(event.save());
            promises.push(result.destroy());

            Parse.Promise.when(promises).then(function(object){
                return Parse.Promise.as(event);
            });

        }).then(function(object){
            Parse.Push.send({
                channels: ["CHN_"+event.id],
                expiration_interval: expirationSeconds,
                data:{
                        alert:"Checkout from ready for:"+event.id,
                        event: {"id": event.id }
                    }
                },
                {
                success: function(bla){
                    return Parse.Promise.as(event);
                    // Push was successful
                    //response.success(bla);
                },
                error: function(e){
                    return Parse.Promise.error(event);
                    //response.error(error);
                }
            });
        });

    }






    this.setState = function(){
    }
    this.startGame = function(eventAndRequest) {
        var _event = eventAndRequest.passedEvent;
        var _request = eventAndRequest.passedRequest;
        if (_event.get("participants").length >= _event.get("facility").get("sport").get("minPlayers")){
            _event.set("state", "blue");
            _event.set("startTime", new Date());
            _event.set("isEnded", false);
        }

        Parse.Push.send({
            channels: ["CHN_"+_event.id],
            expiration_interval: expirationSeconds,
            data:{
                    alert:"Starting event/game:"+_event.id,
                    event: {"id": _event.id, "participants": [] }
                }
            },

            {
            success: function(bla){
            },
            error: function(e){
            }
        });

        return _event.save();

    }

}
module.exports = StateReadyLightblue;