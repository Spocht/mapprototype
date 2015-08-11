package dev.spocht.spocht;



import android.content.Context;
import android.content.SharedPreferences;

import com.parse.Parse;
import com.parse.ParseObject;

import dev.spocht.spocht.data.DataManager;

/**
 * Created by edm on 01.08.15.
 */

public class Application extends android.app.Application {


    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "SpochT";

    // Used to pass location from MainActivity to PostActivity
    public static final String INTENT_EXTRA_LOCATION = "location";

    // Key for saving the search distance preference
    private static final String KEY_SEARCH_DISTANCE = "searchDistance";

    private static final float DEFAULT_SEARCH_DISTANCE = 250.0f;

    private static SharedPreferences preferences;

    //private static ConfigHelper configHelper;

    public Application(){

    }
    @Override
    public void onCreate(){
        System.out.println("OnCreateApplication");
        super.onCreate();
        DataManager.getInstance();
        registerSubclasses();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "IvP2CsQV7fRqfg0tSQs2Ugot9YCDo4VAdRUYsQFd", "I7uNfjct4uL5GMwC8kUiubofsWDVAmzG1CAf0VE0");

    }

    private void registerSubclasses(){
        //ParseObject.registerSubclass(MetaModel.class);
        ParseObject.registerSubclass(MyUser.class);
    }

}
