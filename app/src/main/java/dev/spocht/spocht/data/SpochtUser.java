package dev.spocht.spocht.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
public class SpochtUser extends ParseData {
    public SpochtUser()
    {
        ;
    }
    public SpochtUser(final String name, final String password)
    {
        setUsername(name);
        setPassword(password);
    }
    public SpochtUser(final ParseUser user)
    {
        setUser(user);
    }
    public void setUsername(final String name)
    {
        user().setUsername(name);
    }
    public String getUsername()
    {
        return user().getUsername();
    }
    public void setPassword(final String password)
    {
        user().setPassword(password);
    }
    public void setUser(final ParseUser user) {
        put("user", user);
        if(null != user().getObjectId()) {
            updateAcl();
        }
        setUpdated();
    }
    public ParseUser user()
    {
        ParseUser user;
        if(null == this.getObjectId())
        {
            if(this.has("user"))
            {
                user = (ParseUser) get("user");
            }
            else {
                user = new ParseUser();
                setUser(user);
            }
        }
        else {
            this.fetchIfNeeded();
            if (this.has("user")) {
                user = (ParseUser) get("user");
                if (null == ParseData.fetchMe(user)) {
                    user = new ParseUser();
                }
            }
            else {
                user = new ParseUser();
                setUser(user);
            }
        }
        //todo: check the default username thing
        if(user.get("username")==null)
        {
            Log.d(this.getClass().getCanonicalName(),"John Doe created");
            user.setUsername("John Doe");
        }
        return(user);
    }
    public void updateAclBlocking() throws ParseException {
        ParseACL acl = new ParseACL(user());
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(false);
        setACL(acl);
        save();
        clearUpdated();
    }
    public void updateAcl()
    {
        ParseACL acl = new ParseACL(user());
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(false);
        setACL(acl);
        saveEventually();
        clearUpdated();
    }
    public Date lastSeen()
    {
        Date date= null;
        this.fetchIfNeeded();
        date = (Date)get("lastSeen");
        if(null == date)
        {
            date= new Date();
        }
        return(date);
    }
    public void seen()
    {
        Date date = new Date();
        put("lastSeen", date);
        setUpdated();
    }
    public void setExperience(final Experience xp)
    {
        if(null == xp.getObjectId())
        {
            final SpochtUser user = this;
            xp.saveEventually(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        addUnique("experience", ParseObject.createWithoutData(Experience.class, xp.getObjectId()));
                        if(xp.clearUpdated())
                        {
                            xp.persist();
                        }
                        if((ready())&& (!clrItem()))
                        {//ready for update and no item is left
                            persist();
                            clearUpdated();
                        }
                        else {
                            setUpdated();
                        }
                    } else {
                        Log.e(this.getClass().getCanonicalName(), "Error saving data.", e);
                    }
                }
            });
            setItem();
        }
        else {
            addUnique("experience", ParseObject.createWithoutData(Experience.class, xp.getObjectId()));
            setUpdated();
            setItem();
        }
    }
    public List<Experience> experiences()
    {
        List<Experience> xps=null;
        this.fetchIfNeeded();
        xps = getList("experience");
        if(null == xps)
        {
            xps=new ArrayList<>();
        }
        return(xps);
    }

    public boolean isThisMe(String username) {
        return username.equals(user().getUsername());

    }
}
