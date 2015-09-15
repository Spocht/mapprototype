package dev.spocht.spocht.data;

import java.util.HashMap;

import dev.spocht.spocht.activity.Application;

/**
 * Created by highway on 08/09/15.
 */
public class EventState {
    private static HashMap<String, Integer> mMap = new HashMap<>();
    private static HashMap<String, Integer> mMapTint = new HashMap<>();

    public static boolean EVENT_STATE_TINT = true;
    public static boolean EVENT_STATE_NOTINT = false;

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

    public static int get(String color, boolean tint) {
        int colorReferenceKey = 0;

        if ( ! tint) {
            colorReferenceKey = get(color);
        } else {
            color = color + "tint";

            if (mMapTint.containsKey(color)) {
                colorReferenceKey = mMapTint.get(color);
            } else {
                colorReferenceKey = DataManager.getContext().getResources()
                        .getIdentifier(color, "color", Application.PACKAGE_NAME);
                mMapTint.put(color, colorReferenceKey);
            }
        }

        return colorReferenceKey;
    }

}
