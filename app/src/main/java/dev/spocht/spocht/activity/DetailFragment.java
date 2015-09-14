package dev.spocht.spocht.activity;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Facility;
import dev.spocht.spocht.data.InfoRetriever;

public class DetailFragment extends ListFragment {
    private Facility mFacility;

    private MapsActivity mActivity;
    private EventAdapter mEventAdapter;
    private ImageButton mNewGameImageButton;
    private ImageView mImage;
    private TextView mType;
    private TextView mName;
    private TextView mFieldCount;
    private TextView mComment;

    private int mSibId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FractionalLinearLayout view = (FractionalLinearLayout) inflater.inflate(R.layout.fragment_detail, container, false);

        RelativeLayout infoLayout = (RelativeLayout) view.findViewById(R.id.details_fragment_infoContainer);
        mNewGameImageButton = new ImageButton(getActivity().getBaseContext());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        mNewGameImageButton.setLayoutParams(params);
        mNewGameImageButton.setImageResource(R.drawable.ic_plus_circle_outline_black_24dp);
        mNewGameImageButton.setBackgroundColor(getResources().getColor(R.color.white));

        infoLayout.addView(mNewGameImageButton);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImage       = (ImageView) view.findViewById(R.id.fragment_detail_image);
        mType        = (TextView)  view.findViewById(R.id.fragment_detail_type);
        mName        = (TextView)  view.findViewById(R.id.fragment_detail_title);
        mFieldCount  = (TextView)  view.findViewById(R.id.fragment_detail_fieldCount);
        mComment     = (TextView)  view.findViewById(R.id.fragment_detail_comment);

        mActivity = (MapsActivity) getActivity();
        mFacility = mActivity.getSelectedFacility();
        mEventAdapter = new EventAdapter(mActivity.getApplicationContext(), new ArrayList<Event>());
        mEventAdapter.setNotifyOnChange(false);
        setListAdapter(mEventAdapter);
        Log.d(getClass().getCanonicalName(), "mFacility holds " + String.valueOf(mFacility.events().size()) + " events");
    }

    public void refreshContents() {
        mFacility = mActivity.getSelectedFacility();

        mFacility.updateEvents(new InfoRetriever<Facility>() {
            @Override
            public void operate(Facility facility) {
                setEvents();
            }
        });

        // individual elements are separated into according methods to simplify maintenance
        setImage();
        setTitle();
        setFieldCount();
        setComment();
        setEvents();
        setType();

        Log.d(getClass().getCanonicalName(), "Fields: " + String.valueOf(mFacility.numberOfFields()) + ", events " + mFacility.events().size());

        allowNewGame(mFacility.events().size() < mFacility.numberOfFields());
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshContents();
    }

    /*
     * below are alle the methods used to update this fragments contents
     */
    private void setImage() {
        mImage.setImageBitmap(mFacility.image().picture(new InfoRetriever<Bitmap>() {
            @Override
            public void operate(Bitmap bitmap) {
                mImage.setImageBitmap(bitmap);
            }
        }));
    }

    private void setTitle() {
        mName.setText(mFacility.name());
    }

    private void setFieldCount() {
        int number = mFacility.numberOfFields();
        mFieldCount.setText(
                String.valueOf(number) + " " +
                        (number == 1 ? getString(R.string.field) : getString(R.string.fields))
        );
    }

    private void setComment() {
        mComment.setText(mFacility.comment());
    }

    private void setType() {
        Log.d(getClass().getCanonicalName(), "Trying to find string resource for " + mFacility.sport().name());

        String type = mActivity.getType(mFacility.sport().name());

        // ensure first letter capitalized
        mType.setText(Character.toUpperCase(type.charAt(0)) + type.substring(1));
    }

    private void setEvents() {
        Log.d(getClass().getCanonicalName(), "setEvents()");
        mEventAdapter.clear();
        mEventAdapter.addAll(mFacility.events());
        mEventAdapter.notifyDataSetChanged();
    }

    /**
     * Allow new event
     *
     * Toggles display of CreateNewGame-Button in detail fragment
     *
     * @param allow true to have the button displayed
     */
    public void allowNewGame(boolean allow) {
        if (allow) {
            mNewGameImageButton.setOnClickListener(new View.OnClickListener() {
                public String mEventName;

                @Override
                public void onClick(View v) {

                    Context context = getView().getContext();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(getString(R.string.new_event));

                    LinearLayout layout = new LinearLayout(context);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(lp);
                    layout.setGravity(Gravity.CLIP_VERTICAL);
                    layout.setPadding(2, 2, 2, 2);

                    TextView tv = new TextView(context);
                    tv.setText(getString(R.string.enter_event_name));
                    tv.setPadding(40, 40, 40, 40);
                    tv.setGravity(Gravity.START);
                    tv.setTextSize(20);

                    LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tvParams.bottomMargin = 5;

                    final EditText input = new EditText(getView().getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);

                    layout.addView(tv, tvParams);
                    layout.addView(input, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    builder.setView(layout);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mEventName = input.getText().toString();
                            Event event = mFacility.addEvent(mEventName);
                            event.persist(); //todo review, because there might be race conditions with the facility.addEvent(). It is possible that the item is saved twice
                            event.checkIn(DataManager.getInstance().currentUser());

                            refreshContents();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
        } else {
            mNewGameImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toastWelcome = Toast.makeText(
                            getView().getContext(),
                            getString(R.string.max_allowed_events_reached),
                            Toast.LENGTH_LONG
                    );
                    toastWelcome.show();
                }
            });
        }
    }

}
