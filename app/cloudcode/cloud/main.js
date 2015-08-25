
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.define("spochtRokks", function(request, response) {
	var query = new Parse.Query("MyUser");
	query.equalTo("firstname", request.params.firstname);
	query.find({
		success: function(results) {
			response.success(results);
			//response.success(request.params.firstname);
		},
		error: function() {
			response.error("nope");
		}
	});
	
});
