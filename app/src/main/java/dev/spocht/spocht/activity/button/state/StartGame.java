package dev.spocht.spocht.activity.button.state;

import android.util.Log;
import android.view.View;

import dev.spocht.spocht.R;
import dev.spocht.spocht.activity.button.EventStateImageButton;
import dev.spocht.spocht.data.DataManager;

/**
 * Created by highway on 06/09/15.
 */
public class StartGame extends ImageButton {

    public StartGame(EventStateImageButton context) {
        mContext = context;
    }

    @Override
    public void entry() {
        mContext.setImageResource(R.drawable.ic_play_arrow_black_24dp);

        mContext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getEvent().startGame(DataManager.getInstance().currentUser());
            }
        });

        // let the user checkout using longclick
        mContext.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mContext.getEvent().checkOut(DataManager.getInstance().currentUser());
                mContext.setImageResource(R.drawable.ic_check_black_24dp);

                Log.d(getClass().getCanonicalName(), "Checking me out");
                return true;
            }
        });
    }
}
