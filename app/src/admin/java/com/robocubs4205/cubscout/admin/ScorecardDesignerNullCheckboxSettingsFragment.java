package com.robocubs4205.cubscout.admin;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robocubs4205.cubscout.R;

/**
 * Created by trevor on 11/8/16.
 */

public class ScorecardDesignerNullCheckboxSettingsFragment extends Fragment {
    public ScorecardDesignerNullCheckboxSettingsFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_scorecard_design_null_checkbox_settings,container,false);

        return view;
    }

}
