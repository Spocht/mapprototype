package dev.spocht.spocht;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for a post.
 */
@ParseClassName("Sportsite")
public class Sportsite extends ParseObject {

    public String getText() {
        return getString("name");
    }
    public void setText(String s){
        put("name", s);
    }
    public ParseGeoPoint getLocation(){
        return getParseGeoPoint("location");
    }
    public void setLocation(ParseGeoPoint p) {
        put("location", p);
    }

    public static ParseQuery<Sportsite> getQuery() {
        return ParseQuery.getQuery(Sportsite.class);
    }

}
