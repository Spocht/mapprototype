package dev.spocht.spocht.data;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
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
    public Event(final String name)
    {
        setName(name);
        setState("grey");
    }
    public void setName(final String name)
    {
        put("name", name);
        setUpdated();
    }
    public String name()
    {
        String name = null;
        try {
            this.fetchIfNeeded();
            name = getString("name");
        } catch (ParseException e) {
            Log.e("spocht.data", "Error getting data", e);
        }
        if(null == name)
        {
            name = new String("unknown");
        }
        return (name);
    }
    protected void setStartTime(final Date start)
    {//todo only used, when a location is linked in
        put("startTime",start);
        setUpdated();
    }
    public Date startTime()
    {
        Date date = null;
        try{
            this.fetchIfNeeded();
            date = (Date)get("startTime");
        } catch (ParseException e) {
            Log.e("spocht.data", "Error getting data", e);
        }
        if(null == date)
        {
            date = getUpdatedAt();
        }
        return(date);
    }
    protected void setState(final String state)
    {
        put("state",state);
        setUpdated();
    }
    public String getState()
    {
        String st=null;
        try{
            this.fetchIfNeeded();
            st= getString("state");
        } catch (ParseException e) {
            Log.e("spocht.data", "Error getting data", e);
        }
        if(null == st)
        {
            st = "unknown";
        }
        return(st);
    }

    protected void setParticipation(final Participation participation)
    {
        if(null == participation.getObjectId())
        {
            participation.saveEventually(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        addUnique("participants", ParseObject.createWithoutData(Participation.class, participation.getObjectId()));
                        setUpdated();
                        if(participation.clearUpdated())
                        {
                            participation.persist();
                        }
                    } else {
                        System.out.println("Error while saving participation object");
                    }
                }
            });
        }
        else {
            addUnique("participants", ParseObject.createWithoutData(Participation.class, participation.getObjectId()));
            setUpdated();
        }
    }
    public List<Participation> participants()
    {
        List<Participation> participations=null;
        try {
            this.fetchIfNeeded();
             participations = getList("participants");
            if(null == participations)
            {
                if(this.has("participants")) {
                        participations = this.getParseObject("participation").fetchIfNeeded();
                }
                else
                {
                    participations = new ArrayList<Participation>();
                }
            }
        } catch (com.parse.ParseException e) {
            Log.e("spocht.data", "Error getting data", e);
            participations = new ArrayList<Participation>();
        }
        return(participations);
    }
    protected void removeParticipation(final Participation participation)
    {
        if(null != participation) {
            removeAll("participants", Arrays.asList(participation.getObjectId()));
        }
    }
    public void setFacility(final Facility facility)
    {
        if(null == facility.getObjectId())
        {
            facility.saveEventually(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        put("facility", ParseObject.createWithoutData(Facility.class, facility.getObjectId()));
                        setUpdated();
                        if(facility.clearUpdated())
                        {
                            facility.persist();
                        }
                    } else {
                        System.out.println("Error while saving facility object");
                    }
                }
            });
        } else {
            put("facility", ParseObject.createWithoutData(Facility.class, facility.getObjectId()));
            setUpdated();
        }
    }
    public Facility facility()
    {
        Facility facility =null;
        try{
            this.fetchIfNeeded();
            facility = (Facility)get("facility");
            if(null == facility)
            {
                if(this.has("facility")) {
                    facility = this.getParseObject("facility").fetchIfNeeded();

                }
                else
                {
                    facility = new Facility();
                }
            }
        } catch (com.parse.ParseException e) {
            Log.e("spocht.data", "Error getting data", e);
            facility = new Facility();
        }

        return(facility);
    }

    public Boolean isUserCheckedIn(final SpochtUser user)
    {
        List<Participation> participations = participants();
        for(Participation p: participations)
        {
            if(p.user().getObjectId().equals(user.getObjectId()))
            {
                return true;
            }
        }
        return false;
    }

    ///events to handle
    public void checkIn(final SpochtUser user)
    {
        if(!isUserCheckedIn(user)) {
            Participation participation = new Participation(user, Outcome.GAVEUP);
            participation.persist();
            //todo if successful API call, update local Event
            if(true) {
                DataManager.getInstance().registerPushChannel(this.getObjectId());
                setParticipation(participation);
            }
            else
            {
                participation.destroy();
            }
        }
    }
    public void checkOut(final SpochtUser user)
    {
        //todo: call API
        DataManager.getInstance().unregisterPushChannel(this.getObjectId());
        if(!getState().equals("grey")) {
            //game has not been ended by end()
            List<Participation> participations = participants();
            for (Participation p : participations) {
                if (p.user().getObjectId().equals(user.getObjectId())) {
                    removeParticipation(p);
                    p.setOutcome(Outcome.GAVEUP);
                    p.destroy(); //todo: discuss with team
                    return;
                }
            }
        }
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
