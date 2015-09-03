package dev.spocht.spocht.data;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;

import dev.spocht.spocht.Application;
import dev.spocht.spocht.R;
import dev.spocht.spocht.callbacks.LocationCallback;
import dev.spocht.spocht.listener.MyLocationListener;
import dev.spocht.spocht.mock.location.Lorrainepark;
import dev.spocht.spocht.mock.location.Lorrainestrasse;
import dev.spocht.spocht.mock.location.Spitalacker;
import dev.spocht.spocht.mock.location.Steckweg;
import dev.spocht.spocht.mock.location.Stub;

/**
 * Created by mueller8 on 29.08.2015.
 * No relation to the excellent magazine of the CCC.
 */
public class DatenSchleuder {

    private class ItemUser{
        public String name;
        public String password;
        public ItemUser(String name, String password)
        {
            this.name = name;
            this.password = password;
        }
    }

    static private DatenSchleuder instance = new DatenSchleuder();
    private ArrayList<Facility> lstFacility = new ArrayList<Facility>(10);
    private Sport               sport       = new Sport("tabletennis",2);
    private ArrayList<SpochtUser> lstUser   = new ArrayList<SpochtUser>(10);
    private MyLocationListener myLocationListener;
    private LocationCallback<Void, Location> locationCallback = new LocationCallback<Void, Location>() {
        @Override
        public Void operate(Location l) {
            Log.d("spocht.datenschleuder","Location: "+l.toString());
            if((l.getLatitude() == 10)&&(l.getLongitude() == 10))
            {
                throwInitialData();
            }
            else if((l.getLatitude() == 20)&&(l.getLongitude() == 20))
            {
                throwHistorie();
            }
            else if((l.getLatitude() == 30)&&(l.getLongitude() == 30))
            {
                createUsers();
            }
            return null;
        }
    };

    private DatenSchleuder()
    {
        ;
    }
    public static DatenSchleuder getInstance()
    {
        return instance;
    }

    //SPOCHT-13
    public void setup(Context ctx)
    {
        MyLocationListener.create(ctx);
        MyLocationListener.getInstance().register(locationCallback, false);
        Log.d("spocht.datenschleuder","Setup");
    }


    public void throwInitialData()
    {
        ArrayList<Stub> locations = new ArrayList<>(10);
        locations.add(new Lorrainepark());
        locations.add(new Lorrainestrasse());
        locations.add(new Steckweg());
        locations.add(new Spitalacker());

        sport.persist();
        Image pic = new Image("default", BitmapFactory.decodeResource(DataManager.getInstance().getContext().getResources(), R.drawable.spochtlogo2));
        pic.persist();

        for(Stub s:locations)
        {
            Facility facility = new Facility(s.getName(),new GeoPoint(s.getLatLng().latitude,s.getLatLng().longitude),2,sport);
            facility.setComment(s.getDesc());
            facility.setImage(pic);
            facility.persist();
            lstFacility.add(facility);
        }
        createUsers();
        for(int cnt =5;cnt>0;cnt--) {
            Log.d("spocht.datenschleuder", "__ Let things settle... [" + cnt + "]");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(Facility f:lstFacility)
        {
            f.saveEventually(new SaveCallback() {
                public void done(ParseException e) {
                    if (null != e) {
                        Log.e("spocht.datenschleuder","Error while saving facility object",e);
                    }
                }
            });
        }
    }
    public void createUsers()
    {
        sport.persist();

        ArrayList<ItemUser> users = new ArrayList<>(10);
        users.add(new ItemUser("lugi@lolwut.org","honigimkopf"));
        users.add(new ItemUser("edm@streetparade.ch","atemlos"));
        users.add(new ItemUser("rudi@victoryismi.ne","schwerterkaffee"));
        users.add(new ItemUser("test@parse.com","iossucks"));
        users.add(new ItemUser("gays@microsoft.com","redmond"));

        for(ItemUser u:users)
        {


            SpochtUser user = DataManager.getInstance().signup(u.name,u.password);
            Experience xp = new Experience(sport);
            xp.persist();
            user.setExperience(xp);
            user.seen();
            user.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(null == e)
                    {
                        Log.d("spocht.dataschleuder","User created");
                    }
                    else
                    {
                        Log.e("spocht.dataschleuder","create users",e);
                    }
                }
            });

//            SpochtUser user = new SpochtUser(u.name, u.password);
//            user.user().setEmail(u.name);
//            Experience xp = new Experience(sport);
//            xp.persist();
//            user.setExperience(xp);
//            user.seen();
//
//            try {
//                user.signUp();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

            lstUser.add(user);
        }

    }
    public void createHistorie(final String nameEvent, final Facility facility, final SpochtUser user, final Date date, final Outcome outcome)
    {
        Event event = facility.addEvent(nameEvent);
        event.setStartTime(date);
        Participation participation = new Participation(user, outcome);
        participation.persist();
        event.setParticipation(participation);
        event.setState("orange");
    }
    private class ItemHistory{
        public String name;
        public Facility facility;
        public SpochtUser user;
        public Date date;
        public ItemHistory(final String name, final Facility facility, final SpochtUser user, final Date date)
        {
            this.name=name;
            this.facility = facility;
            this.user = user;
            this.date = date;
        }
    }
    public void throwHistorie()
    {
        ArrayList<ItemHistory> lstHist=new ArrayList<>(5);
        lstHist.add(new ItemHistory("Crack the Table (1)",lstFacility.get(0),lstUser.get(0),new Date()));
        lstHist.add(new ItemHistory("Crack the Table (2)",lstFacility.get(1),lstUser.get(1),new Date()));
        lstHist.add(new ItemHistory("Crack the Table (3)",lstFacility.get(2),lstUser.get(2),new Date()));
        lstHist.add(new ItemHistory("Crack the Table (4)",lstFacility.get(3),lstUser.get(3),new Date()));
        for(ItemHistory i:lstHist)
        {
            createHistorie(i.name,i.facility,i.user,i.date,Outcome.GAVEUP);
        }
    }
}
