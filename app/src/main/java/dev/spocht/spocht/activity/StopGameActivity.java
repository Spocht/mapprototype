package dev.spocht.spocht.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Outcome;

public class StopGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stop_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
