package dev.spocht.spocht.data;

import com.parse.ParseClassName;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Participation")
public class Participation extends ParseData {
    public Participation()
    {//default constructor for Parse.com
        ;
    }
    public Participation(final Outcome result)
    {
        setOutcome(result);
    }
    public void setOutcome(final Outcome result)
    {
        put("outcome",result);
    }
    public Outcome outcome()
    {
        return ((Outcome)get("outcome"));
    }

}
