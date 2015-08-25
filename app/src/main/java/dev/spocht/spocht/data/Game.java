package dev.spocht.spocht.data;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Game")
public class Game extends ParseData {

    @Override
    void retrieve(String id, final InfoRetriever callback) {

        ParseQuery<Game> queryEvent = ParseQuery.getQuery(Game.class);
        queryEvent.getInBackground(id, new GetCallback<Game>() {
            public void done(Game object, ParseException e) {
                if (e == null) {
                    callback.operate(object);
                } else {
                    // something went wrong
                }
            }
        });
    }
}
