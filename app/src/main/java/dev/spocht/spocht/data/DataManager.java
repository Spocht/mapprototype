package dev.spocht.spocht.data;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.List;

import dev.spocht.spocht.MapsActivity;
import dev.spocht.spocht.MyUser;

/**
 * Created by edm on 11.08.15.
 */
public class DataManager {

    private static DataManager instance = null;

    private List<Facility> facilities;


    private DataManager(){

//        ParseObject.registerSubclass(MyUser.class);

//        Parse.enableLocalDatastore(MapsActivity.getAppContext());
//        Parse.initialize(MapsActivity.getAppContext(), "@string/parse_application_id", "@string/parse_client_key");
    }
    public synchronized static DataManager getInstance(){
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }


    public void request(String id, Class<? extends ParseObject> po, InfoRetriever callback) {
        callback.operate(po);
    }
}
