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
}
