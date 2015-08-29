package dev.spocht.spocht.data;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Experience")
public class Experience extends ParseData {
    public Experience()
    {//default constructor for Parse.com
        ;
    }
    protected void setXP(final int val)
    {
        put("xp",val);
    }
    public int xp()
    {
        int xp = getInt("xp");
        if(xp < 0)
        {
            xp = 0;
        }
        return (xp);
    }
    protected void setLevel(final int lvl)
    {
        put("level",lvl);
    }
    public int level()
    {
        int lvl = getInt("level");
        if(lvl < 1)
        {
            lvl = 1;
        }
        return (lvl);
    }
    public int xpForNextLevel()
    {
        return((int)Math.pow(10,level()));
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
        if(xpNew > xpForNextLevel())
        {
            setLevel(level()+1);
        }
    }
}
