Parse.Cloud.job("housekeeping", function(request, status) {
    //Parse.Cloud.useMasterKey();

    var pq = Parse.Object.extend("Event");
    var participationsQuery = new Parse.Query(pq);
    participationsQuery.containedIn("state", ["orange", "lightblue"]);
    var currentDate = new Date();
    var compareDate = new Date(currentDate - 1800 * 1000);
    participationsQuery.lessThan("updatedAt", compareDate);
    var promises = [];
    participationsQuery.find().then(function(results){
        //return results;
        results.forEach(function(event){
            console.log(event);
            event.set("state", "yellow");
            promises.push(event.save());
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