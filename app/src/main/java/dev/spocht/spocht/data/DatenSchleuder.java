package dev.spocht.spocht.data;

import android.graphics.BitmapFactory;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;

import dev.spocht.spocht.R;
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

    private ArrayList<Facility> lstFacility = new ArrayList<Facility>(10);
    private Sport               sport       = new Sport("tabletennis",2);
    private ArrayList<SpochtUser> lstUser   = new ArrayList<SpochtUser>(10);


    public void throwInitialData()
    {
        ArrayList<Stub> locations = new ArrayList<>(10);
        locations.add(new Lorrainepark());
        locations.add(new Lorrainestrasse());
        locations.add(new Steckweg());
        locations.add(new Spitalacker());

        ArrayList<ItemUser> users = new ArrayList<>(10);
        users.add(new ItemUser("lugi@lolwut.org","honigimkopf"));
        users.add(new ItemUser("edm@streetparade.ch","atemlos"));
        users.add(new ItemUser("rudi@victoryismi.ne","schwerterkaffee"));
        users.add(new ItemUser("test@parse.com","iossucks"));
        users.add(new ItemUser("gays@microsoft.com","redmond"));

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
        for(ItemUser u:users)
        {
            SpochtUser user = new SpochtUser(u.name,u.password);
            user.setEmail(u.name);
            user.seen();
            user.setExperience(new Experience(sport));
            try {
                user.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            lstUser.add(user);
        }
        for(int cnt =5;cnt>0;cnt--) {
            System.out.println("__ Let things settle... ["+cnt+"]");
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
                        System.out.println("Error while saving facility object");
                    }
                }
            });
        }
        for(SpochtUser u:lstUser)
        {
            u.saveEventually(new SaveCallback() {
                public void done(ParseException e) {
                    if (null != e) {
                        System.out.println("Error while saving user object");
                    }
                }
            });
        }
    }
    public void createHistorie(final String nameEvent, final Facility facility, final SpochtUser user, final Date date, final Outcome outcome)
    {
        Event event = facility.addEvent(nameEvent);
        event.setStartTime(date);
        Participation participation = new Participation(user, outcome);
        participation.persist();
        event.setParticipation(participation);
    }
    public void throwHistorie()
    {

    }
}
