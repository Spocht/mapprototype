package dev.spocht.spocht.data;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import bolts.Task;
import dev.spocht.spocht.R;
import dev.spocht.spocht.monitor.EventMonitor;

/**
 * Created by edm on 11.08.15.
 */
public class DataManager {
    private static volatile DataManager instance = null;
    private static Context context;

    private DataManager() {
        if (context == null) {
            throw new Error("Oops! Context not set. Please set it first by injectContext");
        }

        ParseObject.registerSubclass(ParseData.class);
        ParseObject.registerSubclass(SpochtUser.class);
        ParseObject.registerSubclass(Sport.class);
        ParseObject.registerSubclass(Invitation.class);
        ParseObject.registerSubclass(Experience.class);
        ParseObject.registerSubclass(Image.class);
        ParseObject.registerSubclass(Participation.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(EventSingle.class);
        ParseObject.registerSubclass(EventTournament.class);
        ParseObject.registerSubclass(EventToDeath.class);
        ParseObject.registerSubclass(Facility.class);

        Parse.enableLocalDatastore(context);
        Parse.initialize(context,
                getContext().getString(R.string.parse_application_id),
                getContext().getString(R.string.parse_client_key)
        );

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });



        registerMonitors();

    }

    //get instance needs more protection than just synchronized.
    //https://en.wikipedia.org/wiki/Singleton_pattern

    public synchronized static DataManager getInstance(){
        //double checked locking... still leads to
        //Parse.enbleLocalDatastore-called-twice-Exceptions
        //when DataManager.geInstance is called in CTOR of DataManager,
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
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
        Task<ParseUser> user=ParseUser.logInInBackground(mail, password);

        try {
            user.waitForCompletion();
        } catch (InterruptedException e) {
            return false;
        }
        return !user.isFaulted();
    }
    public SpochtUser signup(String mail, String password)
    {
        SpochtUser user = new SpochtUser(mail,password);
        user.setEmail(mail);
        user.seen();
        Task<Void> task = user.signUpInBackground();

        try {
            task.waitForCompletion();
        } catch (InterruptedException e) {
            return new SpochtUser();
        }
        if(!task.isFaulted())
        {
            return user;
        }
        else {
            return new SpochtUser();
        }
    }
    public void logout()
    {
        Task<Void> task = ParseUser.logOutInBackground();
        try
        {
            task.waitForCompletion();
        }
        catch(InterruptedException e)
        {
            //todo: crash report?
        }
    }

    public <T extends ParseData> void request(String id, Class<T> obj, final InfoRetriever<T> callback) {

        ParseQuery<T> query = ParseQuery.getQuery(obj);
        query.getInBackground(id, new GetCallback<T>() {
            public void done(T object, ParseException e) {
                if (e == null) {
                    callback.operate(object);
                } else {
                    Log.e("spocht.dataManager","Failed to load items:",e);
                }
            }
        });
    }

    public void findFacilities(final GeoPoint location, final double distance, final InfoRetriever<List<Facility>> callback)
    {
        ParseQuery<Facility> query = ParseQuery.getQuery(Facility.class);
        query.whereWithinKilometers("location", location, distance);
        query.orderByAscending("location");
        query.setLimit(10); //todo: magic number
        query.include("sport");
        query.include("events");
        query.findInBackground(new FindCallback<Facility>() {
            @Override
            public void done(List<Facility> list, ParseException e) {
                if(e == null)
                {
                    callback.operate(list);
                }
                else
                {
                    Log.e("spocht.dataManager", "Failed to load items:", e);
                }
            }
        });
    }

    public static void injectContext(Context ctx) {
        Log.d("spocht.datamanager","Injecting Context: " + ctx);
        context = ctx;
    }

    public Context getContext(){

        return this.context;
    }

    private void registerMonitors(){
        EventMonitor eventMonitor = new EventMonitor(context);
    }
}
