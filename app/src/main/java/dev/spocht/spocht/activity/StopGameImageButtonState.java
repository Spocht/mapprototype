package dev.spocht.spocht.activity;

import android.util.Log;

import dev.spocht.spocht.R;

/**
 * Created by highway on 06/09/15.
 */
public class StopGameImageButtonState extends ImageButtonState {
    public StopGameImageButtonState(NewGameStateImageButton context) {
        mContext = context;
    }

    @Override
    /**
     *  display a snackbar where players can set the outcome
     */
    public void entry() {
        mContext.setImageResource(R.drawable.ic_stop_white_24dp);
        Log.d("StateImageButton", "Saving OutCome");

        mContext.setState(mContext.getNewGameState());
    }
}
