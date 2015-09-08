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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dev.spocht.spocht.Application;
import dev.spocht.spocht.R;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Participation;

/**
 * Created by highway on 03/09/15.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.event_list, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Event event = getItem(position);

        LayoutInflater li = LayoutInflater.from(getContext());
        if (null == convertView) {
            convertView = li.inflate(R.layout.event_list, parent, false);
        }

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

        for (Participation part : event.participants()) {
            View line = li.inflate(R.layout.participant_list, null);

            TextView tv = (TextView) line.findViewById(R.id.fragment_detail_event_participant);
//            tv.setText(part.user().getUsername());
            tv.setText("participation.user().getUsername() would crash me");

            participantList.addView(line);
        }

        ImageButton checkInButton = (ImageButton) convertView.findViewById(R.id.fragment_detail_event_checkinButton);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.checkIn(DataManager.getInstance().currentUser());
                Log.d("DetailFragment", "Check into existing event");
            }
        });

        return convertView;
    }




}
