package dev.spocht.spocht.data;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Event")
public class Event extends ParseData {

    //Facility facility = new Facility();

    @Override
    void retrieve(String id, final InfoRetriever callback) {

        ParseQuery<Event> queryEvent = ParseQuery.getQuery(Event.class);
        queryEvent.getInBackground(id, new GetCallback<Event>() {
            public void done(Event object, ParseException e) {
                if (e == null) {
                    callback.operate(object);
                } else {
                    // something went wrong
                }
            }
        });
    }
}
