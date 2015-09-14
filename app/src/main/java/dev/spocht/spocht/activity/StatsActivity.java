package dev.spocht.spocht.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Experience;
import dev.spocht.spocht.data.InfoRetriever;
import dev.spocht.spocht.data.Participation;
import dev.spocht.spocht.data.SpochtUser;

public class StatsActivity extends ListActivity {
    private SpochtUser mCurrentUser;

    private TextView tvTotal;
    private TextView tvWin;
    private TextView tvLose;
    private TextView tvTie;
    private TextView tvGaveup;
    private TextView tvNoEvents;
    private ProgressBar pb;
    private TextView tvLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stats);
        mCurrentUser = DataManager.getInstance().currentUser();

        tvTotal = (TextView) findViewById(R.id.stats_text_total);
        tvWin = (TextView) findViewById(R.id.stats_text_win);
        tvLose = (TextView) findViewById(R.id.stats_text_lose);
        tvTie = (TextView) findViewById(R.id.stats_text_tie);
        tvGaveup = (TextView) findViewById(R.id.stats_text_gaveup);
        tvNoEvents = (TextView) findViewById(R.id.stats_text_no_events);
        pb = (ProgressBar) findViewById(R.id.stats_level_progress);
        pb.setMax(Experience.XP_NEEDED_PER_LVL);
        tvLevel = (TextView) findViewById(R.id.stats_level_text_current);

        loadGeneralStats();
        loadLevelStats();
    }



    private void loadLevelStats() {
        Log.d(getClass().getCanonicalName(), "XP preload: " + mCurrentUser.experiences().size());

        for (Experience ex :  mCurrentUser.experiences()) {
            // get realtime data
            Log.d(getClass().getCanonicalName(), "XP preload IDs: " + ex.getObjectId());
            DataManager.getInstance().update(ex.getObjectId(), Experience.class, new InfoRetriever<Experience>() {
                @Override
                public void operate(Experience experience) {
                    // this always overwrites level data if there is expercience in more than one sport
                    // however, this prototype only has one. would need to create a ListView adapter for this to work....

                    Log.d(getClass().getCanonicalName(), "XP preload: " + experience.xp() + " next in " + experience.xpForNextLevel());
                    tvLevel.setText(String.valueOf(experience.level()));
                    pb.setProgress(Experience.XP_NEEDED_PER_LVL - experience.xpForNextLevel());
                }
            });
        }
    }

    private void loadGeneralStats() {
        DataManager.getInstance().findParticipation(new InfoRetriever<List<Participation>>() {
            @Override
            public void operate(List<Participation> participations) {
                Log.d(getClass().getCanonicalName(), "found " + participations.size() + " participations");

                int win = 0;
                int lose = 0;
                int tie = 0;
                int gaveup = 0;

                for (Participation part : participations) {
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

                tvTotal.setText(String.valueOf(participations.size()));
                tvWin.setText(String.valueOf(win));
                tvLose.setText(String.valueOf(lose));
                tvTie.setText(String.valueOf(tie));
                tvGaveup.setText(String.valueOf(gaveup));

                loadEventStats(participations);

            }
        });
    }

    private void loadEventStats(List<Participation> list) {
        if (list.size() > 0) {
            StatsAdapter sa = new StatsAdapter(getApplicationContext(), list);
            setListAdapter(sa);
            tvNoEvents.setVisibility(View.GONE);
        } else {
            tvNoEvents.setVisibility(View.VISIBLE);
        }
    }
}
