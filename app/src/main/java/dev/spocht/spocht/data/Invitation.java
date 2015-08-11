package dev.spocht.spocht.data;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Invitation")
public class Invitation extends ParseData {

    @Override
    void retrieve(String id, final InfoRetriever callback) {

        ParseQuery<Invitation> queryEvent = ParseQuery.getQuery(Invitation.class);
        queryEvent.getInBackground(id, new GetCallback<Invitation>() {
            public void done(Invitation object, ParseException e) {
                if (e == null) {
                    callback.operate(object);
                } else {
                    // something went wrong
                }
            }
        });
    }
}
