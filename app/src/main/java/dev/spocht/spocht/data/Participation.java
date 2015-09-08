package dev.spocht.spocht.data;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Participation")
public class Participation extends ParseData {
    public Participation()
    {//default constructor for Parse.com
        ;
    }
    public Participation(final SpochtUser user, final Outcome result)
    {
        setUser(user);
        setOutcome(result);
    }
    public void setOutcome(final Outcome result)
    {
        put("outcome", result.toString());
        setUpdated();
    }
    public Outcome outcome()
    {
        Outcome out=Outcome.GAVEUP;
        try {
            this.fetchIfNeeded();
            out=Outcome.valueOf(getString("outcome"));
        } catch (ParseException e) {
            Log.e(this.getClass().getCanonicalName(), "Error getting data", e);
        }
        return (out);
    }

    public void setUser(final SpochtUser user)
    {
        if(null == user.getObjectId())
        {
            user.saveEventually(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        put("user", ParseObject.createWithoutData(SpochtUser.class, user.getObjectId()));
                        setUpdated();
                        if(user.clearUpdated())
                        {
                            user.persist();
                        }
                    } else {
                        Log.e("spocht.data", "Error saving data.", e);
                    }
                }
            });
        }
        else {
            put("user", ParseObject.createWithoutData(SpochtUser.class, user.getObjectId()));
            setUpdated();
        }
    }
    public SpochtUser user()
    {
        SpochtUser user = null;
        try {
            this.fetchIfNeeded();
            if(this.has("user")) {
                user = (SpochtUser)get("user");
                if(null == user)
                {
                    user = this.getParseObject("SpochtUser").fetchIfNeeded();
                }
            }
            else
            {
                user = new SpochtUser();
            }
        } catch (com.parse.ParseException e) {
            Log.e(this.getClass().getCanonicalName(), "Error getting user", e);
            user = new SpochtUser();
        }
        return(user);
    }
}
