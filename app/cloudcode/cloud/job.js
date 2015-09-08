Parse.Cloud.job("housekeeping", function(request, status) {
    //Parse.Cloud.useMasterKey();

    var query = new Parse.Query("Event");
    query.containedIn("status", ["orange", "blue"]);
    query.each(function(event){
        console.log(event);
        console.log(request);
    }).then(function(){
        status.success();
    });
});