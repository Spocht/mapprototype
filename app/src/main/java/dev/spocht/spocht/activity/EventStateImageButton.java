package dev.spocht.spocht.activity;

import android.content.Context;
import android.widget.ImageButton;

/**
 * Created by highway on 08/09/15.
 */
public class EventStateImageButton extends ImageButton {
    NewEventImageButtonState mNewGameState;
    StartGameImageButtonState mStartGameState;
    StopGameImageButtonState mStopGameState;
    WaitingForPlayersImageButtonState mWaitingForPlayersState;

    public EventStateImageButton(Context context) {
        super(context);


    }
}
