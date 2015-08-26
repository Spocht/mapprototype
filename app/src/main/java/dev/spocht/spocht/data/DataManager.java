package dev.spocht.spocht.data;

import android.content.Context;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import bolts.Task;
import dev.spocht.spocht.Application;
import dev.spocht.spocht.MapsActivity;
import dev.spocht.spocht.MyUser;

/**
 * Created by edm on 11.08.15.
 */
public class DataManager {

    private static DataManager instance = null;

    private List<Facility> facilities;


    private DataManager(){

        ParseObject.registerSubclass(MyUser.class);

        Context c = Application.getContext().getApplicationContext();
        Parse.enableLocalDatastore(c);
        Parse.initialize(Application.getContext().getApplicationContext(),
                "IvP2CsQV7fRqfg0tSQs2Ugot9YCDo4VAdRUYsQFd",
                "I7uNfjct4uL5GMwC8kUiubofsWDVAmzG1CAf0VE0"
        );

    }
    public synchronized static DataManager getInstance(){
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    public Boolean isAnon()
    {
        return(ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser()));
    }
    public Boolean isLoggedIn()
    {
        return(null != ParseUser.getCurrentUser());
    }
    public Boolean login(String mail, String password)
    {
        Task<ParseUser> user=ParseUser.logInInBackground(mail,password);

        try {
            user.waitForCompletion();
        } catch (InterruptedException e) {
            return false;
        }
        return !user.isFaulted();
    }

    public <T extends ParseData> void request(String id, Class<T> obj, final InfoRetriever<T> callback) {

        ParseQuery<T> query = ParseQuery.getQuery(obj);
        query.getInBackground(id, new GetCallback<T>() {
            public void done(T object, ParseException e) {
                if (e == null) {
                    callback.operate(object);
                } else {
                    System.out.println("failed to load items:: "+e.getMessage());
                    // something went wrong
                }
            }
        });
    }
}
