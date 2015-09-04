package dev.spocht.spocht.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.Event;

/**
 * Created by highway on 03/09/15.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.event_list, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_list, parent, false);
        }

        Log.d("EventAdapter", "populating fields for event " + event.name());
        TextView eventName = (TextView) convertView.findViewById(R.id.fragment_detail_event_name);
        TextView eventTime = (TextView) convertView.findViewById(R.id.fragment_detail_event_timeStarted);
        TextView eventPart = (TextView) convertView.findViewById(R.id.fragment_detail_event_participant);

        eventName.setText(event.name());
        eventTime.setText(event.startTime().toString());

        return convertView;
    }
}
