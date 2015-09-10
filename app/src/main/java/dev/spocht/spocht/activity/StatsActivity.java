package dev.spocht.spocht.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.List;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Outcome;
import dev.spocht.spocht.data.Participation;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ParseQuery<Participation> participationQuery = ParseQuery.getQuery(Participation.class);
        participationQuery
                .whereEqualTo("user", DataManager.getInstance().currentUser())
                .whereExists("outcome");

        participationQuery.findInBackground(new FindCallback<Participation>() {
            @Override
            public void done(List<Participation> list, ParseException e) {
                Log.d(getClass().getCanonicalName(), "found " + list.size() + " participations");

                int win    = 0;
                int lose   = 0;
                int tie    = 0;
                int gaveup = 0;

                for (Participation part : list) {
                    switch  (part.outcome()) {
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

                TextView tvTotal  = (TextView) findViewById(R.id.stats_text_total);
                TextView tvWin    = (TextView) findViewById(R.id.stats_text_win);
                TextView tvLose   = (TextView) findViewById(R.id.stats_text_lose);
                TextView tvTie    = (TextView) findViewById(R.id.stats_text_tie);
                TextView tvGaveup = (TextView) findViewById(R.id.stats_text_gaveup);

                tvTotal.setText(String.valueOf(list.size()));
                tvWin.setText(String.valueOf(win));
                tvLose.setText(String.valueOf(lose));
                tvTie.setText(String.valueOf(tie));
                tvGaveup.setText(String.valueOf(gaveup));
            }
        });
    }


}
