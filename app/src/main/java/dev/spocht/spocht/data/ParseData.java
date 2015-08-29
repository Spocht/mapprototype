package dev.spocht.spocht.data;

import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by mueller8 on 11.08.2015.
 */
public abstract class ParseData extends ParseObject {

    public void persist() {
        try {
            this.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
