package dev.spocht.spocht.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import bolts.Task;

/**
 * Created by mueller8 on 29.08.2015.
 */
@ParseClassName("SpochtUser")
public class SpochtUser extends ParseUser {
    public SpochtUser()
    {
        ;
    }
    public SpochtUser(final String name, final String password)
    {
        setUsername(name);
        setPassword(password);
    }
    public Date lastSeen()
    {
        Date date= (Date)get("lastSeen");
        if(null == date)
        {
            date= new Date();
        }
        return(date);
    }
    public void seen()
    {
        Date date = new Date();
        put("lastSeen",date);
        setUpdated();
    }
    public void setExperience(final Experience xp)
    {
        if(null == xp.getObjectId())
        {
            xp.saveEventually(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        addUnique("experience", ParseObject.createWithoutData(Experience.class, xp.getObjectId()));
                        if(xp.clearUpdated())
                        {
                            xp.persist();
                        }
                        setUpdated();
                    } else {
                        System.out.println("Error while saving experience object for ["+getUsername()+"]");
                    }
                }
            });
        }
        else {
            addUnique("experience", ParseObject.createWithoutData(Experience.class, xp.getObjectId()));
            setUpdated();
        }
    }
    public List<Experience> experiences()
    {
        List<Experience> xps = getList("experience");
        if(null == xps)
        {
            try {
                xps = this.getParseObject("experience").fetchIfNeeded();
            }
            catch (com.parse.ParseException e)
            {
                //todo log?!
                xps= new ArrayList<Experience>();
            }
        }
        return(xps);
    }

    //implement stuff of the ParseData Class

    private Boolean updated;
    protected synchronized void setUpdated(){
        updated = true;
    }
    protected synchronized Boolean clearUpdated(){
        Boolean tmp = updated;
        updated = false;
        return tmp;
    }
    public void persist() {
        try {
            save();
        } catch (ParseException e) {
            Log.e("spocht.user", "Error while persisting", e);
        }
    }

    //implement signup to prevent funny stuff
    public void signUp() throws ParseException {
        clearUpdated();
        super.signUp();
    }
    public Task<Void> signUpInBackGround(){
        clearUpdated();
        return super.signUpInBackground();
    }
    public void signUpInBackGround(SignUpCallback cb){
        clearUpdated();
        super.signUpInBackground(cb);
    }
}
