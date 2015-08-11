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
