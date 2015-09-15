package dev.spocht.spocht.activity.button.state;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;

import dev.spocht.spocht.R;
import dev.spocht.spocht.activity.MapsActivity;
import dev.spocht.spocht.activity.StopGameActivity;
import dev.spocht.spocht.activity.button.EventStateImageButton;
import dev.spocht.spocht.data.DataManager;

/**
 * Created by highway on 06/09/15.
 */
public class StopGame extends ImageButton {
    public StopGame(EventStateImageButton context) {
        mContext = context;
    }

    @Override
    /**
     *  display a snackbar where players can set the outcome
     */
    public void entry() {
        mContext.setImageResource(R.drawable.ic_stop_black_24dp);

        mContext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getActivity().startActivity(new Intent(mContext.getActivity(), StopGameActivity.class));

                Log.d(getClass().getCanonicalName(), "Displaying Outcome Dialog");
            }
        });
    }
}
