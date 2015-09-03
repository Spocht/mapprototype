package dev.spocht.spocht;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
        Log.d("spocht.application","OnCreateApplication");
        super.onCreate();
    }


    public static Context getContext() {
        return getContext();
    }

}
