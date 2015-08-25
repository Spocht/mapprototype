package dev.spocht.spocht.data;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Experience")
public class Experience extends ParseData {

    @Override
    void retrieve(String id, final InfoRetriever callback) {

        ParseQuery<Experience> queryEvent = ParseQuery.getQuery(Experience.class);
        queryEvent.getInBackground(id, new GetCallback<Experience>() {
            public void done(Experience object, ParseException e) {
                if (e == null) {
                    callback.operate(object);
                } else {
                    // something went wrong
                }
            }
        });
    }
}
