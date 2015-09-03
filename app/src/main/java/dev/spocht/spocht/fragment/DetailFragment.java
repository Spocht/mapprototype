package dev.spocht.spocht.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.spocht.spocht.R;
import dev.spocht.spocht.layout.FractionalLinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FractionalLinearLayout view = (FractionalLinearLayout) inflater.inflate(R.layout.fragment_detail, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView name = (TextView) getView().findViewById(R.id.fragment_detail_title);
        TextView fieldCount = (TextView) getView().findViewById(R.id.fragment_detail_fieldCount);
        TextView personCount = (TextView) getView().findViewById(R.id.fragment_detail_personCount);
        name.setText("Facility Name");
        fieldCount.setText("Anzahl Felder");
        personCount.setText("Anzahl Personen");
    }
}
