package dev.spocht.spocht.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Experience;
import dev.spocht.spocht.data.Participation;
import dev.spocht.spocht.data.SpochtUser;

public class StatsActivity extends ListActivity {
    SpochtUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stats);
        mCurrentUser = DataManager.getInstance().currentUser();

        loadGeneralStats();
        loadLevelStats();
    }



    private void loadLevelStats() {
        Log.d(getClass().getCanonicalName(), "XP preload: " + mCurrentUser.experiences().size());

        for (ParseObject o :  mCurrentUser.experiences()) {

            // get realtime data
            Log.d(getClass().getCanonicalName(), "XP preload IDs: " + o.getObjectId());
            Experience ex = new Experience();
            ex.setObjectId(o.getObjectId());
            ex.fetchInBackground(new GetCallback<Experience>() {
                @Override
                public void done(Experience parseObject, ParseException e) {
                    // this always overwrites level data if there is expercience in more than one sport
                    // however, this prototype only has one. would need to create a ListView adapter for this to work....

                    Log.d(getClass().getCanonicalName(), "XP preload: " + parseObject.get("xp") + " next in " + parseObject.xpForNextLevel());
                    TextView tvLevel = (TextView) findViewById(R.id.stats_level_text_current);
                    tvLevel.setText(String.valueOf(parseObject.level()));


                    ProgressBar pb = (ProgressBar) findViewById(R.id.stats_level_progress);
                    pb.setMax(Experience.XP_NEEDED_PER_LVL);
                    pb.setProgress(Experience.XP_NEEDED_PER_LVL - parseObject.xpForNextLevel());
                }
            });
        }

    }

    private void loadGeneralStats() {
        ParseQuery<Participation> participationQuery = ParseQuery.getQuery(Participation.class);
        participationQuery
                .whereEqualTo("user", mCurrentUser)
                .whereExists("outcome")
                .orderByDescending("createdAt");

        participationQuery.findInBackground(new FindCallback<Participation>() {
            @Override
            public void done(List<Participation> list, ParseException e) {
                Log.d(getClass().getCanonicalName(), "found " + list.size() + " participations");

                int win = 0;
                int lose = 0;
                int tie = 0;
                int gaveup = 0;

                for (Participation part : list) {
                    switch (part.outcome()) {
                        case WIN:
                            win++;
                            break;
                        case LOSE:
                            lose++;
                            break;
                        case TIE:
                            tie++;
                            break;
                        case GAVEUP:
                            gaveup++;
                            break;
                    }
                    Log.d(getClass().getCanonicalName(), part.getObjectId() + " " + part.outcome().toString() + " event " + part.event().name());
                }

                TextView tvTotal = (TextView) findViewById(R.id.stats_text_total);
                TextView tvWin = (TextView) findViewById(R.id.stats_text_win);
                TextView tvLose = (TextView) findViewById(R.id.stats_text_lose);
                TextView tvTie = (TextView) findViewById(R.id.stats_text_tie);
                TextView tvGaveup = (TextView) findViewById(R.id.stats_text_gaveup);

                tvTotal.setText(String.valueOf(list.size()));
                tvWin.setText(String.valueOf(win));
                tvLose.setText(String.valueOf(lose));
                tvTie.setText(String.valueOf(tie));
                tvGaveup.setText(String.valueOf(gaveup));

                loadEventStats(list);
            }
        });
    }

    private void loadEventStats(List<Participation> list) {
        TextView tvNoEvents = (TextView) findViewById(R.id.stats_text_no_events);

        if (list.size() > 0) {
            StatsAdapter sa = new StatsAdapter(getApplicationContext(), list);
            setListAdapter(sa);
            tvNoEvents.setVisibility(View.GONE);
        } else {
            tvNoEvents.setVisibility(View.VISIBLE);
        }
    }
}
