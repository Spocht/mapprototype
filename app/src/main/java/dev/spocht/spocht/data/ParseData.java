package dev.spocht.spocht.data;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * Created by mueller8 on 11.08.2015.
 */
@ParseClassName("ParseData")
public abstract class ParseData extends ParseObject {
    private Boolean updated;
    protected synchronized void setUpdated(){
        updated = true;
    }
    protected synchronized Boolean clearUpdated(){
        Boolean tmp = updated;
        updated = false;
        return tmp;
    }
    public void load(){
        ParseQuery query = ParseQuery.getQuery(this.getClass());
        try {
            query.get(this.getObjectId());
        } catch (ParseException e) {
            Log.e(this.getClass().getCanonicalName(),"Load failed",e);
        }
    }
    public void persist() {
        try {
            this.pin();
            this.save();
        } catch (ParseException e) {
            Log.e("spocht.object", "Error while persisting", e);
        }
    }
    public void destroy()
    {
        try
        {
            this.unpin();
            this.delete();
        }catch(ParseException e)
        {
            Log.e("spocht.object", "Error while deleting", e);
        }
    }

    public <T extends ParseObject> T fetchIfNeeded(){
        if(this.isDataAvailable()) {
            Log.d(this.getClass().getCanonicalName(), "i am loaded unnecessary [" + this.getObjectId() + "]");
            return (T)this;
        }
        else
        {
            Log.d(this.getClass().getCanonicalName(), "i am loaded [" + this.getObjectId() + "]");
            //todo: maybe we should implement a proper PARSE queue here
            try {
                T tmp = super.fetchIfNeeded();
                tmp.pin();
                return tmp;
            }
            catch (ParseException e)
            {
                Log.e(this.getClass().getCanonicalName(),"Error loading data ["+this.getObjectId()+"]",e);
                return null;
            }
        }
    }
    static <T extends ParseObject> T fetchMe(T obj)
    {
        if(obj.isDataAvailable()) {
            Log.d(obj.getClass().getCanonicalName(), "i am loaded unnecessary [" + obj.getObjectId() + "]");
            return obj;
        }
        else
        {
            Log.d(obj.getClass().getCanonicalName(), "i am loaded [" + obj.getObjectId() + "]");
            //todo: maybe we should implement a proper PARSE queue here
            try {
                T tmp = obj.fetchIfNeeded();
                tmp.pin();
                return tmp;
            }
            catch (ParseException e)
            {
                Log.e(obj.getClass().getCanonicalName(),"Error loading data ["+obj.getObjectId()+"]",e);
                return null;
            }
        }
    }
}
