package dev.spocht.spocht.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Facility;
import dev.spocht.spocht.data.Participation;
import dev.spocht.spocht.layout.FractionalLinearLayout;

public class DetailFragment extends Fragment {
    private Facility mFacility;

    private EventAdapter mEventAdapter;

    private ImageView mImage;
    private TextView mName;
    private TextView mFieldCount;
    private TextView mComment;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Event> tmp = new ArrayList<Event>();
        mEventAdapter = new EventAdapter(getActivity().getApplicationContext(), tmp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FractionalLinearLayout view = (FractionalLinearLayout) inflater.inflate(R.layout.fragment_detail, container, false);
        mListView = (ListView) getActivity().findViewById(R.id.fragment_detail_event_infoContainer);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImage       = (ImageView) view.findViewById(R.id.fragment_detail_image);
        mName        = (TextView)  view.findViewById(R.id.fragment_detail_title);
        mFieldCount  = (TextView)  view.findViewById(R.id.fragment_detail_fieldCount);
        mComment     = (TextView)  view.findViewById(R.id.fragment_detail_comment);


    }

    public void refreshContents() {
        // individual elements are separated into according methods to simplify maintenance
        setImage();
        setTitle();
        setFieldCount();
        setComment();
        setEvents();
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshContents();
    }

    public void setFacility(Facility facility) {
        mFacility = facility;
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

    private void setEvents() {
        mEventAdapter.clear();
        Log.d("EventApapter", "before: " + String.valueOf(mEventAdapter.getCount()));

        int count = mFacility.events().size();
        Log.d("EventAdapter", "mFacility holds " + String.valueOf(count) + " events");
        mEventAdapter.addAll(mFacility.events());
        Log.d("EventApapter", "after: " + String.valueOf(mEventAdapter.getCount()));
        mEventAdapter.notifyDataSetChanged();
    }
}
