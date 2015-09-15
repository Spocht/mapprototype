package dev.spocht.spocht.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Outcome;

public class StopGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_game);
    }

    public void onWinClick(View view) {
        Event event = DataManager.getInstance().getEventMonitor().event();
        event.stopGame(DataManager.getInstance().currentUser(), Outcome.WIN);

        onStop();

        Log.d(getClass().getCanonicalName(), "I\'m a WINNER on " + event.getObjectId());
    }

    public void onLoseClick(View view) {
        Event event = DataManager.getInstance().getEventMonitor().event();
        event.stopGame(DataManager.getInstance().currentUser(), Outcome.LOSE);

        onStop();

        Log.d(getClass().getCanonicalName(), "I\'m a LOSER on " + event.getObjectId());
    }

    public void onTieClick(View view) {
        Event event = DataManager.getInstance().getEventMonitor().event();
        event.stopGame(DataManager.getInstance().currentUser(), Outcome.TIE);

        onStop();

        Log.d(getClass().getCanonicalName(), "It\'s a TIE on " + event.getObjectId());
    }

    public void onGiveUpClick(View view) {
        Event event = DataManager.getInstance().getEventMonitor().event();
        event.stopGame(DataManager.getInstance().currentUser(), Outcome.GAVEUP);

        onStop();
        Log.d(getClass().getCanonicalName(), "I GIVE UP on " + event.getObjectId());
    }
}
