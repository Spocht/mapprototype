package dev.spocht.spocht.data;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
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

/**
 * Created by edm on 11.08.15.
 */
public class DataManager {
    private static volatile DataManager instance = null;
    private static Context context;
    private SpochtUser currentUser;
    private EventMonitor eventMonitor;

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

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


        registerPushChannel("");

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
        if(null != ParseUser.getCurrentUser())
        {
            try {
                loadUser();
            } catch (ParseException e) {
                Log.e("spocht.dataManager","Fail to fetch user",e);
            }
            return true;
        }
        return(false);
    }
    public void loadUser() throws ParseException {
        ParseQuery<SpochtUser> query = ParseQuery.getQuery(SpochtUser.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.include("user");
        currentUser = query.getFirst();
        currentUser.pin();
    }
    public Boolean login(String mail, String password)
    {
        Task<ParseUser> user=ParseUser.logInInBackground(mail, password);

        try {
            user.waitForCompletion();
            if(!user.isFaulted()) {
                loadUser();
            }
        } catch (InterruptedException e) {
            return false;
        } catch (ParseException e) {
            Log.e(this.getClass().getCanonicalName(),"Login",e);
            return false;
        }
        return !user.isFaulted();
    }
    public SpochtUser signup(String mail, String password)
    {
        SpochtUser user = new SpochtUser(mail,password);
        user.user().setEmail(mail);
        user.seen();
        try {
            user.user().signUp();
            user.updateAclBlocking();
            user.pin();
            return user;
        } catch (ParseException e) {
            Log.e(this.getClass().getCanonicalName(),"SignUp Failed",e);
            return new SpochtUser();
        }
    }
    public void logout()
    {
        currentUser().unpinInBackground();
        flushLocalStore();
        Task<Void> task = ParseUser.logOutInBackground();
        try
        {
            task.waitForCompletion();
        }
        catch(InterruptedException e)
        {
            Log.e(this.getClass().getCanonicalName(),"Logout failed ",e);
        }
    }
    public void flushLocalStore()
    {
        findFacilitiesLocal(new GeoPoint(), -1, new InfoRetriever<List<Facility>>() {
            @Override
            public void operate(List<Facility> facilities) {
                Facility.unpinAllInBackground(facilities);
            }
        });
    }
    public <T extends ParseData> void request(String id, Class<T> obj, final InfoRetriever<T> callback) {
        ParseObject.createWithoutData(obj, id).fetchIfNeededInBackground(new GetCallback<T>() {
            @Override
            public void done(T parseObject, ParseException e) {
                if (e == null) {
                    parseObject.pinInBackground();
                    callback.operate(parseObject);
                } else {
                    Log.e(this.getClass().getCanonicalName(), "Failed to load items:", e);
                }
            }
        });
    }
    public <T extends ParseData> void update(String id, Class<T> obj, final InfoRetriever<T> callback) {
        ParseObject.createWithoutData(obj, id).fetchInBackground(new GetCallback<T>() {
            @Override
            public void done(T parseObject, ParseException e) {
                if (e == null) {
                    parseObject.pinInBackground();
                    callback.operate(parseObject);
                } else {
                    Log.e(this.getClass().getCanonicalName(), "Failed to load items:", e);
                }
            }
        });
    }
    public void findFacilitiesLocal(final GeoPoint location, final double distance, final InfoRetriever<List<Facility>> callback)
    {
        Log.d(this.getClass().getCanonicalName(), "Find Facilities locally @ " + location);
        ParseQuery<Facility> query = ParseQuery.getQuery(Facility.class);
        if(distance > 0) {
            query.whereWithinKilometers("location", location, distance);
        }
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Facility>() {
            @Override
            public void done(List<Facility> list, ParseException e) {
                if (null == e) {
                    if (null != list) {
                        callback.operate(list);
                    }
                } else {
                    Log.e(this.getClass().getCanonicalName(), "Error finding facilities", e);
                }
            }
        });
    }
    public void findFacilitiesRemote(final GeoPoint location, final double distance, final InfoRetriever<List<Facility>> callback)
    {
        Log.d(this.getClass().getCanonicalName(), "Find Facilities remote @ " + location);
        ParseQuery<Facility> query = ParseQuery.getQuery(Facility.class);
        query.whereWithinKilometers("location", location, distance);
        query.findInBackground(new FindCallback<Facility>() {
            @Override
            public void done(List<Facility> list, ParseException e) {
                if (e == null) {
                    Facility.pinAllInBackground(list);
                    callback.operate(list);
                } else {
                    Log.e(this.getClass().getCanonicalName(), "Error finding facilities:", e);
                }
            }
        });
    }

    public void findFacilities(final GeoPoint location, final double distance, final InfoRetriever<List<Facility>> callback)
    {
        Log.d(this.getClass().getCanonicalName(), "Find Facilities @ " + location);
        findFacilitiesLocal(location, distance, callback);
        findFacilitiesRemote(location,distance,callback);
    }

    public static void injectContext(Context ctx) {
        Log.d("spocht.data.dataManager", "Injecting Context: " + ctx);
        context = ctx;
    }

    public static Context getContext(){

        return context;
    }

    public SpochtUser currentUser()
    {
        if(currentUser == null)
        {
            return new SpochtUser();
        }
        return currentUser;
    }

    private void registerMonitors(){
        eventMonitor = new EventMonitor(context);
    }
    public EventMonitor getEventMonitor()
    {
        return eventMonitor;
    }
    public void registerPushChannel(final String name)
    {
        if(null != name) {
            ParsePush.subscribeInBackground(name, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d(this.getClass().getCanonicalName(), "Push ["+name+"]: successfully subscribed to the channel.");
                    } else {
                        Log.e(this.getClass().getCanonicalName(), "Push ["+name+"]: failed to subscribe", e);
                    }
                }
            });
        }
        else
        {
            Log.e(this.getClass().getCanonicalName(),"Push registration failed! Null Pointer");
        }
    }
    public void unregisterPushChannel(final String name)
    {
        if(null != name) {
            ParsePush.unsubscribeInBackground(name, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d(this.getClass().getCanonicalName(), "Push ["+name+"]: successfully unsubscribed to the channel.");
                    } else {
                        Log.e(this.getClass().getCanonicalName(), "Push ["+name+"]: failed to unsubscribe", e);
                    }
                }
            });
        }
        else
        {
            Log.e(this.getClass().getCanonicalName(),"Push registration failed! Null Pointer");
        }
    }
}
