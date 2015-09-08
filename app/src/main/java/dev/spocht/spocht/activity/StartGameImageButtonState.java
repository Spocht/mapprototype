package dev.spocht.spocht.activity;

import android.util.Log;
import android.view.View;

import dev.spocht.spocht.R;

/**
 * Created by highway on 06/09/15.
 */
public class StartGameImageButtonState extends ImageButtonState {

    public StartGameImageButtonState(NewGameStateImageButton context) {
        mContext = context;
    }

    @Override
    public void onClick(View view) {
        mContext.setState(mContext.getStopGameState());
    }

    @Override
    public void entry() {
        mContext.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        Log.d("StateImageButton", "Saving OutCome");
    }
}
