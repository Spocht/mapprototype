package dev.spocht.spocht.activity.button.state;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import dev.spocht.spocht.R;
import dev.spocht.spocht.activity.button.EventStateImageButton;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Event;

/**
 * Created by highway on 06/09/15.
 */
public class WaitingForPlayers extends ImageButton {
    public WaitingForPlayers(EventStateImageButton context) {
        mContext = context;
    }

    @Override
    public void entry() {

        if (mContext.amICheckedIn()) {
            mContext.setImageResource(R.drawable.ic_hourglass_empty_black_24dp);

            // display a normal message
            mContext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toastWelcome = Toast.makeText(
                            v.getContext(),
                            DataManager.getContext().getString(R.string.cannot_checkin_again),
                            Toast.LENGTH_LONG
                    );
                    toastWelcome.show();
                    Log.d(getClass().getCanonicalName(), "cannot check in again");
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
        } else {
            mContext.setImageResource(R.drawable.ic_check_black_24dp);

            mContext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.getEvent().checkIn(DataManager.getInstance().currentUser());

                    Log.d(getClass().getCanonicalName(), "Checking me in again");
                }
            });

        }
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onReceivePush(Event event) {
    }
}
