package dev.spocht.spocht.data;

import java.util.HashMap;
import java.util.Map;

import dev.spocht.spocht.Application;

/**
 * Created by highway on 08/09/15.
 */
public class EventState {
    private static HashMap<String, Integer> mMap = new HashMap<>();

    public static int get(String color) {
        int colorReferenceKey = 0;

        if (mMap.containsKey(color)) {
            colorReferenceKey = mMap.get(color);
        } else {
            colorReferenceKey = DataManager.getContext().getResources()
                    .getIdentifier(color, "color", Application.PACKAGE_NAME);
            mMap.put(color, colorReferenceKey);
        }

        return colorReferenceKey;
    }

}
