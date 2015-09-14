package dev.spocht.spocht.data;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Event")
public class Event extends ParseData {

    public Event()
    {//default constructor for Parse.com
        ;
    }
    public Event(final String name)
    {
        setName(name);
        setState("orange");
        setIsEnded(false);
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
    protected void setStartTime(final Date start)
    {//todo only used, when a location is linked in
        put("startTime", start);
        setUpdated();
    }
    public Date startTime()
    {
        Date date = null;
        this.fetchIfNeeded();
        date = (Date)get("startTime");
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
        this.fetchIfNeeded();
        st= getString("state");
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
                        Log.e(this.getClass().getCanonicalName(), "Error saving data.", e);
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
        this.fetchIfNeeded();
        participations = getList("participants");
        if(null == participations)
        {
            participations=new ArrayList<>();
        }
        return(participations);
    }
    protected void removeParticipation(final Participation participation) {
        if (null != participation) {
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
                        Log.e(this.getClass().getCanonicalName(), "Error saving data.", e);
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
        this.fetchIfNeeded();
        facility = (Facility)get("facility");
        return(facility);
    }
    public void end()
    {
        setIsEnded(true);
    }

    public void setIsEnded(boolean isEnded) {
        put("isEnded", isEnded);
        setUpdated();
    }

    public boolean getIsEnded() {
        boolean res=false;
        this.fetchIfNeeded();
        if(has("isEnded")) {
            res = getBoolean("isEnded");
        }
        else
        {
            res=false;
            setIsEnded(false);
        }
        return res;
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
        //when participants is empty this somehow NPEs
        //cloudcode also checks for already logged in users,
        //so for now this if is deactivated.
        //consult with rudee!
        //if(!isUserCheckedIn(user)) {
            //todo if successful API call, update local Event
            try {
                System.out.println("Calling cloud function: checkin");
                //The GeoFence does not work when app is restarted whilst one is checked in.
                DataManager.getInstance().registerPushChannel(this.getObjectId());
                DataManager.getInstance().getEventMonitor().setEvent(this);
                ParseCloud.callFunction("checkin", generateParameterMap(user));

            } catch (ParseException e) {
                Log.e(this.getClass().getCanonicalName(),"error at checkin in "+this.name(),e);
            }
        //}
    }
    public void checkOut(final SpochtUser user)
    {
        try {
            System.out.println("Calling cloud function: checkout");
            ParseCloud.callFunction("checkout", generateParameterMap(user));
            DataManager.getInstance().unregisterPushChannel(this.getObjectId());
            DataManager.getInstance().getEventMonitor().setEvent(null);
        } catch (ParseException e) {
            Log.e(this.getClass().getCanonicalName(),"error at checkout in "+this.name(),e);
        }

    }

    public void startGame(final SpochtUser user) {
        try {
            System.out.println("Calling cloud function: startGame");
            ParseCloud.callFunction("startGame", generateParameterMap(user));
        } catch (ParseException e) {
            Log.e(this.getClass().getCanonicalName(), "error at startGame in " + this.name(), e);
        }
    }

    public void stopGame(final SpochtUser user, Outcome outcome){
        try {
            System.out.println("Calling cloud function: stopGame");
            Map<String, String> mappedOutcome = new HashMap<>();
            mappedOutcome.put("value", outcome.toString());
            ParseCloud.callFunction("stopGame", generateParameterMap(user).put("outcome", mappedOutcome));
            DataManager.getInstance().unregisterPushChannel(this.getObjectId());
            DataManager.getInstance().getEventMonitor().setEvent(null);
        } catch (ParseException e) {
            Log.e(this.getClass().getCanonicalName(),"error at stopGame in "+this.name(),e);
        }
    }

    private Map<String, Map<String, String>> generateParameterMap(final SpochtUser user){
        Map<String, Map<String, String>> params = new HashMap<>();
        Map<String, String> mappedEvent = new HashMap<>();
        mappedEvent.put("id", this.getObjectId());
        Map<String, String> mappedUser = new HashMap<>();
        mappedUser.put("id", user.getObjectId());
        params.put("event", mappedEvent );
        params.put("user", mappedUser);
        return params;
    };
    public void start()
    {

    }
}
