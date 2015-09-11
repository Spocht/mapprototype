package dev.spocht.spocht.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dev.spocht.spocht.Application;
import dev.spocht.spocht.R;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.EventState;
import dev.spocht.spocht.data.Participation;
import dev.spocht.spocht.data.SpochtUser;

/**
 * Created by highway on 03/09/15.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.event_list, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater li = LayoutInflater.from(getContext());
        if (null == convertView) {
            convertView = li.inflate(R.layout.event_list, parent, false);
            convertView.setId(position);
        }

        final Event event = getItem(position);

        Log.d(this.getClass().getCanonicalName(),"View ["+position+"]: "+event.name());

        int bgColor = EventState.get(event.getState());
        if (0 == bgColor) {
            bgColor = R.color.white;
        }

        convertView.setBackgroundColor(convertView.getResources().getColor(bgColor));

        Log.d("EventAdapter", "populating fields for event " + event.name());
        TextView eventName = (TextView) convertView.findViewById(R.id.fragment_detail_event_name);
        TextView eventTime = (TextView) convertView.findViewById(R.id.fragment_detail_event_timeStarted);

        eventName.setText(event.name());
        String startedAt = DataManager.getContext().getString(R.string.started_at);
        String startedAtDate = null;
        try {
            startedAtDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(event.startTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        eventTime.setText(startedAt + " " + startedAtDate);

        // participants as list
        LinearLayout participantList = (LinearLayout) convertView.findViewById(R.id.fragment_detail_event_participant_list);
        participantList.removeAllViews();

        String currentUsername = DataManager.getInstance().currentUser().getUsername();
        boolean isAlreadyCheckedIn = false;

        for (Participation part : event.participants()) {
            Log.d(getClass().getCanonicalName(), "adding participation");
            View line = li.inflate(R.layout.participant_list, null);

            TextView tv = (TextView) line.findViewById(R.id.fragment_detail_event_participant);

            SpochtUser user = part.user();
            String username = user.getUsername();
            if (user.isThisMe(currentUsername)) {
                tv.setText("- " + username + " " + DataManager.getContext().getString(R.string.thats_me));
                isAlreadyCheckedIn = true;
            } else {
                tv.setText("- " + username);
            }

            participantList.addView(line);
        }

        ImageButton checkInButton = (ImageButton) convertView.findViewById(R.id.fragment_detail_event_checkinButton);

        if (isAlreadyCheckedIn) {
            checkInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toastWelcome = Toast.makeText(
                            v.getContext(),
                            DataManager.getContext().getString(R.string.cannot_checkin_again),
                            Toast.LENGTH_LONG
                    );
                    toastWelcome.show();
                    Log.d(getClass().getCanonicalName(), "cannot check in again");
                }
            });
        } else {
            checkInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event.checkIn(DataManager.getInstance().currentUser());

                    Log.d(getClass().getCanonicalName(), "Check into existing event");
                }
            });
        }


        return convertView;
    }




}
