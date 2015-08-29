package dev.spocht.spocht.data;

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
        put("outcome", result);
    }
    public Outcome outcome()
    {
        return ((Outcome)get("outcome"));
    }

    public void setUser(final SpochtUser user)
    {
        if(null == user.getObjectId())
        {
            user.saveEventually(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        put("user", ParseObject.createWithoutData(SpochtUser.class, user.getObjectId()));
                    } else {
                        System.out.println("Error while saving sport object");
                    }
                }
            });
        }
        else {
            put("user", ParseObject.createWithoutData(SpochtUser.class, user.getObjectId()));
        }
    }
    public SpochtUser user()
    {
        SpochtUser user = (SpochtUser)get("user");
        if(null == user)
        {
            try {
                user = this.getParseObject("user").fetchIfNeeded();
            }
            catch (com.parse.ParseException e)
            {
                //todo log?!
                user = new SpochtUser();
            }
        }
        return(user);
    }
}