package dev.spocht.spocht.data;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Experience")
public class Experience extends ParseData {
    public static final int XP_NEEDED_PER_LVL = 100;

    public Experience()
    {//default constructor for Parse.com
        ;
    }
    public Experience(final Sport sport)
    {
        setLevel(1);
        setXP(0);
        setSport(sport);
    }
    protected void setXP(final int val)
    {
        put("xp",val);
        setUpdated();
    }
    public int xp()
    {
        int xp = 0;
        this.fetchIfNeeded();
        xp = getInt("xp");
        if(xp < 0)
        {
            xp = 0;
        }
        return (xp);
    }
    protected void setLevel(final int lvl)
    {
        put("level",lvl);
        setUpdated();
    }
    public int level()
    {
        int lvl = 0;
        this.fetchIfNeeded();
        lvl = getInt("level");
        if(lvl < 1)
        {
            lvl = 1;
        }
        return (lvl);
    }
    public int xpForNextLevel()
    {
        return XP_NEEDED_PER_LVL - (xp() % XP_NEEDED_PER_LVL)   ;
    }
    public void addXP(final int val)
    {
        int xpNew = val;
        if(xpNew < 0)
        {
            xpNew = xp();
        }
        else
        {
            xpNew += xp();
        }
        setXP(xpNew);
        if(xpNew > xpForNextLevel()) {
            setLevel(level() + 1);
        }
    }
    public void setSport(final Sport sport)
    {
        if(null == sport.getObjectId())
        {
            sport.saveEventually(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        put("sport", ParseObject.createWithoutData(Sport.class, sport.getObjectId()));
                        setUpdated();
                        if(sport.clearUpdated())
                        {
                            sport.persist();
                        }
                    } else {
                        Log.e(this.getClass().getCanonicalName(),"Error saving data.",e);
                    }
                }
            });
        } else {
            put("sport", ParseObject.createWithoutData(Sport.class, sport.getObjectId()));
            setUpdated();
        }
    }
    public Sport sport()
    {
        Sport sport = null;
        this.fetchIfNeeded();
        sport = (Sport)get("sport");
        return(sport);
    }

}
