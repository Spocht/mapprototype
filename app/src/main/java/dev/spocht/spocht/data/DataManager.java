package dev.spocht.spocht.data;

import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

import bolts.Task;
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

        Parse.enableLocalDatastore(MapsActivity.getAppContext());
        Parse.initialize(MapsActivity.getAppContext(), "IvP2CsQV7fRqfg0tSQs2Ugot9YCDo4VAdRUYsQFd", "I7uNfjct4uL5GMwC8kUiubofsWDVAmzG1CAf0VE0");

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

    public void request(String id, Class<? extends ParseData> obj, final InfoRetriever callback) {
        try {
            obj.newInstance().retrieve(id,callback);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
