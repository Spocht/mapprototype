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
import dev.spocht.spocht.R;
import dev.spocht.spocht.monitor.EventMonitor;

/**
 * Created by edm on 11.08.15.
 */
public class DataManager {

    private static DataManager instance = null;

    private static Context context;


    private DataManager(){


        if (context == null) {
            throw new Error("Oops! Context not set. Please set it first by injectContext");
        }

        ParseObject.registerSubclass(SpochtUser.class);
        ParseObject.registerSubclass(Sport.class);
        ParseObject.registerSubclass(Invitation.class);
        ParseObject.registerSubclass(Experience.class);
        ParseObject.registerSubclass(Image.class);
        ParseObject.registerSubclass(Participation.class);
        ParseObject.registerSubclass(EventSingle.class);
        ParseObject.registerSubclass(EventTournament.class);
        ParseObject.registerSubclass(EventToDeath.class);
        ParseObject.registerSubclass(Facility.class);

        Parse.enableLocalDatastore(context);
        Parse.initialize(context,
                getContext().getString(R.string.parse_application_id),
                getContext().getString(R.string.parse_client_key)
        );

        registerMonitors();

    }

    //get instance needs more protection than just synchronized.

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

    public static void injectContext(Context ctx) {
        System.out.println("Injecting Context: " + ctx);
        context = ctx;
    }

    public Context getContext(){

        return this.context;
    }

    private void registerMonitors(){
        EventMonitor eventMonitor = new EventMonitor(context);
    }

}
