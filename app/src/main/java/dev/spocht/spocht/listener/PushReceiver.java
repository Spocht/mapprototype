package dev.spocht.spocht.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
        Log.d("spocht.push","I received some stuff... " + intent.toString());
        if(intent.hasExtra("com.parse.Data")) {
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                if(json.has("alert"))
                {
                    Log.d("spocht.push","Alert: " + json.getString("alert"));
                }
                if(json.has("event"))
                {
                    JSONObject content = json.getJSONObject("event");
                    Log.d("spocht.push","Update of Event "+ content.getString("id"));
                }

            } catch (JSONException e) {
                Log.e("spocht.push","JSONException",e);
            }
        }
    }
}
