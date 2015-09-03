package dev.spocht.spocht.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.List;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Facility;
import dev.spocht.spocht.data.Participation;
import dev.spocht.spocht.layout.FractionalLinearLayout;

public class DetailFragment extends Fragment {
    private Facility mFacility;

    private ImageView mImage;
    private TextView mName;
    private TextView mFieldCount;
    private TextView mComment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FractionalLinearLayout view = (FractionalLinearLayout) inflater.inflate(R.layout.fragment_detail, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImage       = (ImageView) view.findViewById(R.id.fragment_detail_image);
        mName        = (TextView)  view.findViewById(R.id.fragment_detail_title);
        mFieldCount  = (TextView)  view.findViewById(R.id.fragment_detail_fieldCount);
        mComment = (TextView)  view.findViewById(R.id.fragment_detail_comment);
    }

    public void refreshContents() {
        // individual elements are separated into according methods to simplify maintenance
        setImage();
        setTitle();
        setFieldCount();
        setComment();
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshContents();
    }

    public void setFacility(Facility facility) {
        mFacility = facility;
    }

    /**
     * CheckIn Button click handler
     *
     * @param view
     */
    public void buttonCheckInClick(View view) {
        // todo: click has to invoke a short animation
        Log.d("DetailFragment", "User wants to check-In");
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
        mFieldCount.setText(String.valueOf(number) + (number == 1 ? R.string.field : R.string.fields));
    }

    private void setComment() {
        mComment.setText(mFacility.comment());
    }
}
