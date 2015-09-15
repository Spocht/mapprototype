Parse.Cloud.job("housekeeping", function(request, status) {
    //Parse.Cloud.useMasterKey();

    var expirationSeconds = 300;

    var pq = Parse.Object.extend("Event");
    var participationsQuery = new Parse.Query(pq);
    participationsQuery.containedIn("state", ["orange", "lightblue", "yellow"]);
    var currentDate = new Date();
    var compareDate = new Date(currentDate - 5 * 1000);
    participationsQuery.lessThan("updatedAt", compareDate);




    var promises = [];


    function pushStateToYellow(event){
        var promise = new Parse.Promise();
        Parse.Push.send({
            expiration_interval: expirationSeconds,
            channels: ["CHN_"+event.id],
            data:{
                    alert:"Changing state to yellow for event"+event.id,
                    event: {"id": event.id, "participants": [] }
                }
            },

            {
            success: function(bla){
                promise.resolve(bla);
            },
            error: function(e){
                promise.error(e);
            }
        });
        return promise;
    }

    function pushStateToGrey(event){
        event.set("state", "grey");
        var greyPromises = [];

        event.get("participants").forEach(function(result) {
            var participation = Parse.Object.extend("Participation");
            var participationInstance = new participation;
            participationInstance.id = result.id;
            console.log("Participations loop");
            console.log(participationInstance);
            greyPromises.push(participationInstance.destroy());
        });

        return Parse.Promise.when(greyPromises).then(function(object){
            console.log("Promises resolved");
            return Parse.Promise.as();
        }).then(function(object){
            console.log("EVENTUNSET");
            console.log(event);
            event.unset("participants");
            event.save().then(function(object){
                console.log("EVENTUNSETTED");
                console.log(event);
                return Parse.Promise.as();
            });
        }).then(function(object){
            var promise = new Parse.Promise();
            Parse.Push.send({
                expiration_interval: expirationSeconds,
                channels: ["CHN_"+event.id],
                data:{
                        alert:"Periodic job changed state to grey for event"+event.id,
                        event: {"id": event.id, "participants": [] }
                    }
                },

                {
                success: function(bla){
                    promise.resolve(bla);
                },
                error: function(e){
                    promise.error(e);
                }
            });
            return promise;
        });
    }

    participationsQuery.find().then(function(results){
        results.forEach(function(event){
            if (event.get("state") == "yellow") {
                console.log("it was yellow");
                promises.push(pushStateToGrey(event));
            } else {
                event.set("state", "yellow");
                promises.push(event.save());
                promises.push(pushStateToYellow(event));
            }
        });
        return Parse.Promise.as();
    }).then(function(object){
        Parse.Promise.when(promises).then(function(object){
            status.success("Done");
        }, function(error){
            status.error(error);
        }

        );
    });

});