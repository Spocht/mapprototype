package dev.spocht.spocht.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.parse.ParseClassName;
import com.parse.ParseObject;

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
    public void setPicture(final Bitmap pic)
    {
        put("picture",pic);
    }
    public Bitmap picture()
    {
        Bitmap pic = (Bitmap)get("picture");
        if(null == pic)
        {
            pic = BitmapFactory.decodeResource(DataManager.getInstance().getContext().getResources(), R.drawable.spochtlogo2);
        }
        return (pic);
    }
}
