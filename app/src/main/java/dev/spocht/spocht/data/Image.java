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
    private Bitmap mPicture=null;
    public Image()
    {//default constructor for Parse.com
        ;
    }
    public Image(final String name, final Bitmap pic)
    {
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
        this.fetchIfNeeded();
        name = getString("name");
        if(null == name)
        {
            name = new String("unknown");
        }
        return (name);
    }
    public void setPicture(final Bitmap pic)
    {
        mPicture=pic;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        ParseFile imgFile = new ParseFile (this.name(), data);
        try {
            imgFile.save();
        } catch (ParseException e) {
            Log.e(this.getClass().getCanonicalName(), "fail to store picture", e);
        }
        put("picture",imgFile);
        setUpdated();
    }
    public Bitmap picture(final InfoRetriever<Bitmap> callback)
    {
        if(null == mPicture) {
            ParseFile imgFile = null;
            this.fetchIfNeeded();
            imgFile = (ParseFile) get("picture");
            if (null != imgFile) {
                imgFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        if(null == e) {
                            Log.d(this.getClass().getCanonicalName(), "load finished");
                            mPicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            callback.operate(mPicture);
                        }
                        else
                        {
                            Log.e(this.getClass().getCanonicalName(),"Error loading image",e);
                        }
                    }
                });
            }
            mPicture= BitmapFactory.decodeResource(DataManager.getInstance().getContext().getResources(), R.drawable.spochtlogo2);
        }

        return (mPicture);
    }
}
