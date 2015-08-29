package dev.spocht.spocht.data;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by mueller8 on 29.08.2015.
 */
@ParseClassName("SpochtUser")
public class SpochtUser extends ParseUser {
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
    }
}
