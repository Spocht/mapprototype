package dev.spocht.spocht.activity;

import android.util.Log;
import android.view.View;
import android.widget.*;

import dev.spocht.spocht.R;

/**
 * Created by highway on 06/09/15.
 */
public class NewEventImageButtonState extends ImageButtonState {
    public NewEventImageButtonState(StateImageButton context) {
        mContext = context;
    }

    @Override
    public void entry() {
        Log.d("StateImageButton", "CreatingNewEvent");

        mContext.setImageResource(R.drawable.ic_new_releases_white_24dp);
    }

    @Override
    public void onClick(View view) {
        mContext.setState(mContext.getWaitingForPlayersState());
    }
}
