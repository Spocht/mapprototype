package dev.spocht.spocht.data;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Facility")
public class Facility extends ParseData {

    @Override
    void retrieve(String id, final InfoRetriever callback) {

        ParseQuery<Facility> queryEvent = ParseQuery.getQuery(Facility.class);
        queryEvent.getInBackground(id, new GetCallback<Facility>() {
            public void done(Facility object, ParseException e) {
                if (e == null) {
                    callback.operate(object);
                } else {
                    // something went wrong
                }
            }
        });
    }
}
