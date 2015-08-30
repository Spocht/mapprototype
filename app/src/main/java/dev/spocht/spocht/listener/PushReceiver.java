package dev.spocht.spocht.listener;

import android.content.Context;
import android.content.Intent;

import com.parse.ParseBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mueller8 on 30.08.2015.
 */
public class PushReceiver extends ParseBroadcastReceiver {
    private static final String TAG = "PushReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("I received some stuff... " + intent.getDataString() + "..." + intent.toString());
        if(intent.hasExtra("com.parse.Data")) {
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                if(json.has("alert"))
                {
                    System.out.println("Alert: " + json.getString("alert"));
                }

            } catch (JSONException e) {
                System.out.println("JSONException: " + e.getMessage());
            }
        }
    }
}
