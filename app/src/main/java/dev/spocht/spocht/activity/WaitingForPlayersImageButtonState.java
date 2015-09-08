package dev.spocht.spocht.activity;

import android.util.Log;
import android.view.View;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.Event;

/**
 * Created by highway on 06/09/15.
 */
public class WaitingForPlayersImageButtonState extends ImageButtonState {
    public WaitingForPlayersImageButtonState(StateImageButton context) {
        mContext = context;
    }

    @Override
    public void entry() {
        Log.d("StateImageButton", "Waiting for Players");
        mContext.setImageResource(R.drawable.ic_hourglass_empty_black_24dp);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onReceivePush(Event event) {
        Log.d("StateImageButton", "Received a push message");

        if (event.participants().size() >= event.facility().sport().minPlayers()) {
            mContext.setState(mContext.getStartGameState());
        }
    }
}
