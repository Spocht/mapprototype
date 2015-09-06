package dev.spocht.spocht.activity;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dev.spocht.spocht.Application;
import dev.spocht.spocht.R;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Facility;
import dev.spocht.spocht.layout.FractionalLinearLayout;

public class DetailFragment extends ListFragment {
    private Facility mFacility;

    private MapsActivity mActivity;
    private EventAdapter mEventAdapter;

    private ImageView mImage;
    private TextView mType;
    private TextView mName;
    private TextView mFieldCount;
    private TextView mComment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FractionalLinearLayout view = (FractionalLinearLayout) inflater.inflate(R.layout.fragment_detail, container, false);

        RelativeLayout infoLayout = (RelativeLayout) view.findViewById(R.id.details_fragment_infoContainer);
        StateImageButton sib = new StateImageButton(getActivity().getBaseContext());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        sib.setLayoutParams(params);

        infoLayout.addView(sib);

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
        ArrayList<Event> events = (ArrayList<Event>) mFacility.events();

        mEventAdapter = new EventAdapter(mActivity.getApplicationContext(), events);
        setListAdapter(mEventAdapter);


        int count = mFacility.events().size();
        Log.d("EventAdapter", "mFacility holds " + String.valueOf(count) + " events");

        setImage();
        setTitle();
        setFieldCount();
        setComment();
        setEvents();
    }

    public void refreshContents() {
        mFacility = mActivity.getSelectedFacility();
        // individual elements are separated into according methods to simplify maintenance
        setImage();
        setTitle();
        setFieldCount();
        setComment();
        setEvents();
        setType();
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
        mImage.setImageBitmap(mFacility.image().picture());
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
        Log.d("DetailFragment", "Trying to find string resource for " + mFacility.sport().name());

        String type = (String) getResources().getText(
            getResources().getIdentifier(
                mFacility.sport().name(),
                "string",
                Application.PACKAGE_NAME
            )
        );

        // ensure first letter capitalized
        mType.setText(Character.toUpperCase(type.charAt(0)) + type.substring(1));
    }

    private void setEvents() {
        mEventAdapter.clear();
        mEventAdapter.addAll(mFacility.events());
        mEventAdapter.notifyDataSetChanged();
    }
}
