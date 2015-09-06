package dev.spocht.spocht.activity;

import android.app.ActionBar;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.Event;

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

        mNewGameState = new NewEventImageButtonState(this);
        mStartGameState = new StartGameImageButtonState(this);
        mStopGameState = new StopGameImageButtonState(this);
        mWaitingForPlayersState = new WaitingForPlayersImageButtonState(this);

        mCurrentState = mNewGameState;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOperate(v);
            }
        });
        setImageResource(R.drawable.ic_new_releases_black_24dp);

    }

    public void onClickOperate(View view) {
        mCurrentState.onClick(view);
    }

    public void onReceivePush(Event event) {
        mCurrentState.onReceivePush(event);
    }

    public void setState(ImageButtonState newState) {
        mCurrentState.exit();
        mCurrentState = newState;
        mCurrentState.entry();
    }

    public NewEventImageButtonState getNewGameState() {
        return mNewGameState;
    }

    public StartGameImageButtonState getStartGameState() {
        return mStartGameState;
    }

    public StopGameImageButtonState getStopGameState() {
        return mStopGameState;
    }

    public WaitingForPlayersImageButtonState getWaitingForPlayersState() {
        return mWaitingForPlayersState;
    }
}
