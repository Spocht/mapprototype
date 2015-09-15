package dev.spocht.spocht.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.Facility;
import dev.spocht.spocht.data.InfoRetriever;

public class PictureFragment extends Fragment {

    public PictureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final MapsActivity mapsActivity = (MapsActivity) getActivity();
        Facility facility = mapsActivity.getSelectedFacility();

        final ImageView iv = (ImageView) view.findViewById(R.id.fragment_picture);
        iv.setImageBitmap(facility.image().picture(new InfoRetriever<Bitmap>() {
            @Override
            public void operate(Bitmap bitmap) {
                iv.setImageBitmap(bitmap);
            }
        }));

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsActivity.getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
