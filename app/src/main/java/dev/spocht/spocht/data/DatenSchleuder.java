package dev.spocht.spocht.data;

import android.graphics.BitmapFactory;

import java.util.ArrayList;

import dev.spocht.spocht.R;
import dev.spocht.spocht.mock.location.Lorrainepark;
import dev.spocht.spocht.mock.location.Lorrainestrasse;
import dev.spocht.spocht.mock.location.Spitalacker;
import dev.spocht.spocht.mock.location.Steckweg;

/**
 * Created by mueller8 on 29.08.2015.
 * No relation to the excellent magazine of the CCC.
 */
public class DatenSchleuder {
    private ArrayList<Facility> lstFacility = new ArrayList<Facility>(10);
    private Sport               sport       = new Sport("tabletennis",2);


    public void throwInitialData()
    {
        sport.persist();
        Image pic = new Image("default", BitmapFactory.decodeResource(DataManager.getInstance().getContext().getResources(), R.drawable.spochtlogo2));
        pic.persist();
        Facility facility = new Facility(Lorrainepark.NAME,new GeoPoint(Lorrainepark.LATITUDE,Lorrainepark.LONGITUDE),2,sport);
        facility.setComment(Lorrainepark.DESCRIPTION);
        facility.setImage(pic);
        facility.persist();
        lstFacility.add(facility);
        facility = new Facility(Lorrainestrasse.NAME,new GeoPoint(Lorrainestrasse.LATITUDE,Lorrainestrasse.LONGITUDE),2,sport);
        facility.setComment(Lorrainestrasse.DESCRIPTION);
        facility.setImage(pic);
        facility.persist();
        lstFacility.add(facility);
        facility = new Facility(Spitalacker.NAME,new GeoPoint(Spitalacker.LATITUDE,Spitalacker.LONGITUDE),2,sport);
        facility.setComment(Spitalacker.DESCRIPTION);
        facility.setImage(pic);
        facility.persist();
        lstFacility.add(facility);
        facility = new Facility(Steckweg.NAME,new GeoPoint(Steckweg.LATITUDE,Steckweg.LONGITUDE),2,sport);
        facility.setComment(Steckweg.DESCRIPTION);
        facility.setImage(pic);
        facility.persist();
        lstFacility.add(facility);

    }
}
