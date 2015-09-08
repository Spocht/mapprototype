package dev.spocht.spocht.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseBroadcastReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.InfoRetriever;

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
                    Log.d("spocht.push", "Update of Event " + content.getString("id"));

                    //this is used for the stopGame-event.
                    //if a game is stopped, the participants are removed from the event in cloudcode,
                    //and cannot be retrieved later in operate(Event event). thus, this info is
                    //available in participants... only when stopGame is called.
                    //other cloudcode-functions return an empty array because
                    //the participants-filed in Event has data in it.
                    //also possible to remove the participant from the event on the client, i.e.here.
                    //but as of now this data is sent always... we might need it.
                    final JSONArray participants = content.getJSONArray("participants");

                    DataManager.getInstance().update(content.getString("id"), Event.class, new InfoRetriever<Event>() {
                        @Override
                        public void operate(Event event) {
                            Log.d("spocht.push.eventstate", event.getState());
                            //must integrage the participants stuff.
                            //consult with roo-dee!
                            Log.d("spocht.push.eventpart", participants.toString());

                            //this somehow memleaks when called more than once.
                            //but anyway i am not sure as of yet if this is intended to be
                            //called here. must consult with roo-dee.
                            //DataManager.getInstance().getEventMonitor().setEvent(event);
                            //todo: notify GUI about the update
                        }
                    });

                }

            } catch (JSONException e) {
                Log.e("spocht.push","JSONException",e);
            }
        }
    }
}
