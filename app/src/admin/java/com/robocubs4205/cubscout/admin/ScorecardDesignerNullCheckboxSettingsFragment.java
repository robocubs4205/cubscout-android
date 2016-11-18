package com.robocubs4205.cubscout.admin;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.robocubs4205.cubscout.JSONSerializableInputFragment;
import com.robocubs4205.cubscout.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by trevor on 11/8/16.
 */

public class ScorecardDesignerNullCheckboxSettingsFragment extends Fragment implements JSONSerializableInputFragment {
    public ScorecardDesignerNullCheckboxSettingsFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_scorecard_design_null_checkbox_settings,container,false);

        Spinner nullWhenSpinner = (Spinner)view.findViewById(R.id.null_when_spinner);
        ArrayAdapter<CharSequence> nullWhenSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.null_when_spinner_options,android.R.layout.simple_spinner_item);
        nullWhenSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nullWhenSpinner.setAdapter(nullWhenSpinnerAdapter);

        return view;
    }
    @Override
    public JSONObject serialize() throws JSONException {
        String checkboxMessage = ((EditText)getView().findViewById(R.id.checkbox_message_field)).getText().toString();
        String[] nullWhenOptions = getResources().getStringArray(R.array.null_when_spinner_options);
        int nullWhenIndex = ((Spinner)getView().findViewById(R.id.null_when_spinner)).getSelectedItemPosition();
        String nullWhen = nullWhenOptions[nullWhenIndex];

        return new JSONObject().put("null_when",nullWhen).put("checkbox_message",checkboxMessage);
    }
}
