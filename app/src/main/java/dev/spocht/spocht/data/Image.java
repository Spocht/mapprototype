package dev.spocht.spocht.data;

import android.graphics.Picture;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by mueller8 on 29.08.2015.
 */
@ParseClassName("Image")
public class Image extends ParseObject {
    public Image()
    {//default constructor for Parse.com
        ;
    }
    public Image(final String name, final Picture pic)
    {
        //todo: revise the picture datatype, use a JAVA type or an other platform indepentend one.
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
    public void setPicture(final Picture pic)
    {
        put("picture",pic);
    }
    public Picture picture()
    {
        Picture pic = (Picture)get("picture");
        if(null == pic)
        {
            pic = new Picture();
        }
        return (pic);
    }
}
