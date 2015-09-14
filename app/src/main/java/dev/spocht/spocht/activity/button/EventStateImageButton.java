package dev.spocht.spocht.activity.button;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import dev.spocht.spocht.R;
import dev.spocht.spocht.activity.button.state.*;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Participation;

/**
 * Created by highway on 08/09/15.
 */
public class EventStateImageButton extends android.widget.ImageButton {
    StartGame mStartGameState;
    StopGame mStopGameState;
    WaitingForPlayers mWaitingForPlayersState;

    ImageButton mCurrentState;


    Activity mActivity;

    boolean amICheckedIn = false;
    Event mEvent;

    public EventStateImageButton(Context context) {
        super(context, null);
    }

    public EventStateImageButton(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public EventStateImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EventStateImageButton,
                0, 0);


    }

    public void setState(ImageButton state) {
        // state might be null at first
        if (null != mCurrentState) {
            mCurrentState.exit();
        }

        mCurrentState = state;

        mCurrentState.entry();
    }

    public StartGame getStartGameState() {
        if (null ==  mStartGameState) {
            mStartGameState = new StartGame(this);
        }

        return mStartGameState;
    }

    public StopGame getStopGameState() {
        if (null == mStopGameState) {
            mStopGameState = new StopGame(this);
        }

        return mStopGameState;
    }

    public WaitingForPlayers getWaitingForPlayersState() {
        if (null == mWaitingForPlayersState) {
            mWaitingForPlayersState = new WaitingForPlayers(this);
        }

        return mWaitingForPlayersState;
    }

    public Event getEvent() {
        return mEvent;
    }

    public void setEvent(Event mEvent) {
        this.mEvent = mEvent;
    }

    public boolean amICheckedIn() {
        return amICheckedIn;
    }

    public void setAmICheckedIn(boolean amICheckedIn) {
        this.amICheckedIn = amICheckedIn;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }
}
