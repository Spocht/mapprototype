package dev.spocht.spocht.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Participation;

/**
 * Created by highway on 11/09/15.
 */
public class StatsAdapter extends ArrayAdapter<Participation> {
    public StatsAdapter(Context context, List<Participation> objects) {
        super(context, R.layout.event_list_stats,  objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(getContext());
        if (null == convertView) {
            convertView = li.inflate(R.layout.event_list_stats, parent, false);
            convertView.setId(position);
        }

        final Participation participation = getItem(position);

        // find data about event and opponent
        Event e = participation.event();
        Log.d(getClass().getCanonicalName(), "this is me:" + participation.getObjectId());
        for (Participation p : e.participants()) {
            if (p != participation) {
                Log.d(getClass().getCanonicalName(), "Found opponing participation: " + p.getObjectId());
                TextView tvOpponent = (TextView) convertView.findViewById(R.id.stats_text_opponent);
                tvOpponent.setText(p.user().getUsername());
            } else {
                Log.d(getClass().getCanonicalName(), "might be me" + p.getObjectId());
            }
        }

        TextView tvEventName = (TextView) convertView.findViewById(R.id.stats_text_eventname);
        TextView tvEventDate = (TextView) convertView.findViewById(R.id.stats_text_eventdate);
        tvEventName.setText(participation.event().facility().name());
        String startedAtDate = "";
        try {
            startedAtDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(participation.event().startTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tvEventDate.setText(startedAtDate);

        ImageView ivOutcome = (ImageView) convertView.findViewById(R.id.stats_icon_outcome);

        switch (participation.outcome()) {
            case WIN:
                ivOutcome.setImageResource(R.drawable.ic_thumb_up_white_24dp);
                break;
            case LOSE:
                ivOutcome.setImageResource(R.drawable.ic_thumb_down_white_24dp);
                break;
            case TIE:
                ivOutcome.setImageResource(R.drawable.ic_thumbs_up_down_white_24dp);
                break;
            case GAVEUP:
                ivOutcome.setImageResource(R.drawable.ic_close_circle_white_24dp);
                break;
        }

        return convertView;
    }
}
