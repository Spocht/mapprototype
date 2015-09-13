package dev.spocht.spocht.data;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Sport")
public class Sport extends ParseData {
    public Sport()
    {//default constructor for Parse.com
        ;
    }
    public Sport(final String name, final int minPlayers)
    {
        setName(name);
        setMinPlayers(minPlayers);
    }
    public void setName(final String name)
    {
        put("name", name);
        setUpdated();
    }
    public String name()
    {
        String name = null;
        this.fetchIfNeeded();
        name = getString("name");
        if(null == name)
        {
            name = new String("unknown");
        }
        return (name);
    }
    public void setMinPlayers(final int minPlayers)
    {
        put("minPlayers", minPlayers);
        setUpdated();
    }
    public int minPlayers()
    {
        int min = 0;
        this.fetchIfNeeded();
        min=getInt("minPlayers");
        if(min <0)
        {
            min=0;
        }
        return (min);
    }
}
