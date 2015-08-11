package dev.spocht.spocht;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by edm on 06.08.15.
 */
@ParseClassName("MyUser")
public class MyUser extends ParseObject {

    public MyUser(){}






    public void store(String firstname, String name){
        put("firstname", firstname);
        put("name", name);
        saveInBackground();
    }
    public static ParseQuery<MyUser> getQuery() {
        return ParseQuery.getQuery(MyUser.class);
    }

}
