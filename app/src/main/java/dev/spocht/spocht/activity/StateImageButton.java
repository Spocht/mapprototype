package dev.spocht.spocht.activity;

import android.app.ActionBar;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import dev.spocht.spocht.R;

/**
 * Created by highway on 06/09/15.
 */
public class StateImageButton extends ImageButton {
    NewEventImageButtonState mNewGameState;
    StartGameImageButtonState mStartGameState;
    StopGameImageButtonState mStopGameState;
    WaitingForPlayersImageButtonState mWaitingForPlayersState;

    ImageButtonState mCurrentState;

    public StateImageButton(Context context) {
        super(context);


        setImageResource(R.drawable.ic_new_releases_black_24dp);

    }

    public void onClick(View view) {

    }

    public void onReceivePush() {

    }
}
