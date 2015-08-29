package dev.spocht.spocht.data;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Event")
public class Event extends ParseData {

//    private enum EventState
//    {
//        CLOSED("grey"),
//        OPEN("orange"),
//        DESERTED("yellow"),
//        READY("lightblue"),
//        PLAYING("blue");
//
//        private String color;
//        private EventState(final String color)
//        {
//            this.color = color;
//        }
//        public String color()
//        {
//            return(color);
//        }
//        static public void create(String name)
//        {
//
//        }
//    }


    public Event()
    {//default constructor for Parse.com
        ;
    }
    public void setName(final String name)
    {
        put("name", name);
    }
    public String name()
    {
        String name = getString("name");
        if(null == name)
        {
            name = "unknown";
        }
        return (name);
    }
    protected void setStartTime(final Date start)
    {//todo only used, when a location is linked in
        put("startTime",start);

    }
    public Date startTime()
    {
        Date date = (Date)get("startTime");
        if(null == date)
        {
            date = getUpdatedAt();
        }
        return(date);
    }
    protected void setState(final String state)
    {
        put("state",state);
    }
    public String getState()
    {
        String st = getString("state");
        if(null == st)
        {
            st = "unknown";
        }
        return(st);
    }




    ///events to handle
    public void checkIn(final SpochtUser user)
    {

    }
    public void checkOut(final SpochtUser user)
    {

    }
    public void start()
    {

    }
    public void end()
    {

    }



//    public void setGame(Game game)
//    {
//        addUnique("games", ParseObject.createWithoutData(Game.class, game.getObjectId()));
//    }
//    public List<Game> getGames()
//    {
//        List<Game> games=getList("games");
//        if(null == games)
//        {
//            try {
//                games = this.getParseObject("game").fetchIfNeeded();
//            }
//            catch (com.parse.ParseException e)
//            {
//                //todo log?!
//                games= new ArrayList<Game>();
//            }
//        }
//        return games;
//    }
}
