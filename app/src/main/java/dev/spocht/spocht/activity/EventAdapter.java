package dev.spocht.spocht.activity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dev.spocht.spocht.R;
import dev.spocht.spocht.activity.button.EventStateImageButton;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.EventState;
import dev.spocht.spocht.data.Participation;
import dev.spocht.spocht.data.SpochtUser;

/**
 * Created by highway on 03/09/15.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    Activity mActivity;

    public EventAdapter(Context context, Activity activity, ArrayList<Event> events) {
        super(context, R.layout.event_list, events);

        mActivity = activity;
    }

    @Override
    public void add(Event object) {
        super.add(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater li = LayoutInflater.from(getContext());
        if (null == convertView) {
            convertView = li.inflate(R.layout.event_list, parent, false);
            convertView.setId(position);
        }

        final Event event = getItem(position);

        Log.d(this.getClass().getCanonicalName(), "View [" + position + "]: " + event.name());

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


        // create button based upon event state
        EventStateImageButton checkInButton = (EventStateImageButton) convertView.findViewById(R.id.fragment_detail_event_checkinButton);
        checkInButton.setActivity(mActivity);
        checkInButton.setEvent(event);
        checkInButton.setAmICheckedIn(isAlreadyCheckedIn);

        switch (event.getState()) {
            case "lightblue":
                checkInButton.setState(checkInButton.getStartGameState());
                break;
            case "blue":
                checkInButton.setState(checkInButton.getStopGameState());
                break;
            default:
                //orange is default
                checkInButton.setState(checkInButton.getWaitingForPlayersState());
                break;
        }

        return convertView;
    }
}
