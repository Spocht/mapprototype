Parse.Cloud.job("housekeeping", function(request, status) {
    //Parse.Cloud.useMasterKey();

    var expirationSeconds = 300;

    var pq = Parse.Object.extend("Event");
    var participationsQuery = new Parse.Query(pq);
    participationsQuery.containedIn("state", ["orange", "lightblue"]);
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

    participationsQuery.find().then(function(results){
        results.forEach(function(event){
            console.log(event);
            event.set("state", "yellow");
            promises.push(event.save());
            promises.push(pushStateToYellow(event));
        });
        return Parse.Promise.as(results);
    }).then(function(object){
        Parse.Promise.when(promises).then(function(object){
            status.success("Done");
        }, function(error){
            status.error(error);
        }

        );
    });

});