package dev.spocht.spocht.data;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Sport")
public class Sport extends ParseData {

    @Override
    void retrieve(String id, final InfoRetriever callback) {

        ParseQuery<Sport> queryEvent = ParseQuery.getQuery(Sport.class);
        queryEvent.getInBackground(id, new GetCallback<Sport>() {
            public void done(Sport object, ParseException e) {
                if (e == null) {
                    callback.operate(object);
                } else {
                    // something went wrong
                }
            }
        });
    }
}
