package com.robocubs4205.cubscout.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.robocubs4205.cubscout.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by trevor on 10/14/16.
 */

public class ScorecardDesignerScoreFieldFragment extends ScorecardDesignerFragment
{
    ScorecardDesignerNullCheckboxSettingsFragment settingsFragment;

    public ScorecardDesignerScoreFieldFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scorecard_design_scored_value,container,false);
        view.findViewById(R.id.remove_button).setOnClickListener(new RemoveButtonOnClickListener());
        view.findViewById(R.id.up_button).setOnClickListener(new UpButtonOnClickListener());
        view.findViewById(R.id.down_button).setOnClickListener(new DownButtonOnClickListener());

        settingsFragment = (ScorecardDesignerNullCheckboxSettingsFragment)getChildFragmentManager().findFragmentById(R.id.null_checkbox_settings_container);

        Spinner typeSpinner = (Spinner)view.findViewById(R.id.key_type_spinner);
        ArrayAdapter<CharSequence> typeSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.scored_field_type_spinner_options,android.R.layout.simple_spinner_item);
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeSpinnerAdapter);

        Spinner nullableSpinner = (Spinner)view.findViewById(R.id.nullable_spinner);
        ArrayAdapter<CharSequence> nullableSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.nullable_spinner_options,android.R.layout.simple_spinner_item);
        nullableSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nullableSpinner.setAdapter(nullableSpinnerAdapter);
        nullableSpinner.setOnItemSelectedListener(new NullableDropdownOnItemSelectedListener());

        return view;
    }
    @Override
    public JSONObject serialize() throws JSONException {
        String name = ((EditText)getView().findViewById(R.id.score_name_field)).getText().toString();

        String[] typeStrings = getResources().getStringArray(R.array.scored_field_type_json_options);

        int typeSelection = ((Spinner)getView().findViewById(R.id.key_type_spinner)).getSelectedItemPosition();
        String type = typeStrings[typeSelection];
        int nullableSelection = ((Spinner)getView().findViewById(R.id.nullable_spinner)).getSelectedItemPosition();
        Boolean isNullable = nullableSelection==getResources().getInteger(R.integer.is_nullable_index);
        JSONObject result = new JSONObject().put("section_type","field").put("field_name",name).put("type",type).put("isNullable",isNullable);
        if(settingsFragment!=null)
        {
            result.put("nullable-settings",settingsFragment.serialize());
        }
        return result;
    }

    class NullableDropdownOnItemSelectedListener implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItem, int position, long id)
        {
            if(position == getResources().getInteger(R.integer.is_nullable_index))
            {
                if(settingsFragment == null)
                {
                    settingsFragment = new ScorecardDesignerNullCheckboxSettingsFragment();
                    getChildFragmentManager().beginTransaction().add(R.id.null_checkbox_settings_container,settingsFragment).commit();
                }
                else
                {
                    getChildFragmentManager().beginTransaction().show(settingsFragment).commit();
                }

            }
            else if(settingsFragment!=null)
            {
                getChildFragmentManager().beginTransaction().hide(settingsFragment).commit();
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
            if(settingsFragment!=null)
            getChildFragmentManager().beginTransaction().hide(settingsFragment).commit();
        }
    }

}
