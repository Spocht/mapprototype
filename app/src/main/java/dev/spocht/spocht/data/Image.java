package dev.spocht.spocht.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

import dev.spocht.spocht.Application;
import dev.spocht.spocht.R;

/**
 * Created by mueller8 on 29.08.2015.
 */
@ParseClassName("Image")
public class Image extends ParseData {
    public Image()
    {//default constructor for Parse.com
        ;
    }
    public Image(final String name, final Bitmap pic)
    {
        //todo: revise the picture datatype, use a JAVA type or an other platform indepentend one.
        setName(name);
        setPicture(pic);
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
            Log.e(this.getClass().getCanonicalName(), "Error getting data", e);
        }
        if(null == name)
        {
            name = new String("unknown");
        }
        return (name);
    }
    public void setPicture(final Bitmap pic)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        ParseFile imgFile = new ParseFile (this.name(), data);
        try {
            imgFile.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        put("picture",imgFile);
        setUpdated();
    }
    public Bitmap picture()
    {
        ParseFile imgFile = null;
        Bitmap pic=null;
        try {
            this.fetchIfNeeded();
            imgFile = (ParseFile)get("picture");
            if(null != imgFile) {
                    byte[] data = imgFile.getData();
                    pic = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        } catch (ParseException e) {
            Log.e(this.getClass().getCanonicalName(), "Error getting data", e);
        }

        if(null == pic)
        {
            pic = BitmapFactory.decodeResource(DataManager.getInstance().getContext().getResources(), R.drawable.spochtlogo2);
        }
        return (pic);
    }
}
