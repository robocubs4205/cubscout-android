package com.robocubs4205.cubscout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

@Deprecated
public class DefenseRatingFragment extends Fragment {

    public DefenseRatingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_defense_rating, container, false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.defense_ratings,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner defenseRatingSpinner = (Spinner)layout.findViewById(R.id.defense_rating_spinner);
        defenseRatingSpinner.setAdapter(adapter);
        return layout;
    }
}
