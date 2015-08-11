package dev.spocht.spocht.data;

import com.parse.Parse;

/**
 * Created by edm on 11.08.15.
 */
public class DataManager {

    private static DataManager instance = null;


    private DataManager(){
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "IvP2CsQV7fRqfg0tSQs2Ugot9YCDo4VAdRUYsQFd", "I7uNfjct4uL5GMwC8kUiubofsWDVAmzG1CAf0VE0");

    }
    public synchronized static DataManager getInstance(){
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void request(String id, Class c, InfoRetriever<?> callback) {

    }
}
