package com.robocubs4205.cubscout.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.robocubs4205.cubscout.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by trevor on 10/14/16.
 */

public class ScoredKeyFragment extends GameDesignerFragment
{

    public ScoredKeyFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_scored_key,container,false);
        view.findViewById(R.id.remove_button).setOnClickListener(new RemoveButtonOnClickListener());
        view.findViewById(R.id.up_button).setOnClickListener(new UpButtonOnClickListener());
        view.findViewById(R.id.down_button).setOnClickListener(new DownButtonOnClickListener());

        Spinner typeSpinner = (Spinner)view.findViewById(R.id.key_type_spinner);
        ArrayAdapter<CharSequence> typeSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.scored_key_types,android.R.layout.simple_spinner_item);
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeSpinnerAdapter);

        return view;
    }
    @Override
    public JSONObject Serialize()
    {
        String name = ((EditText)getView().findViewById(R.id.name_field)).getText().toString();
        String type = ((Spinner)getView().findViewById(R.id.key_type_spinner)).getSelectedItem().toString();
        String nullableString = ((Spinner)getView().findViewById(R.id.key_type_spinner)).getSelectedItem().toString();
        Boolean isNullable;
        if(nullableString.equals("Yes"))
            isNullable = true;
        else
            isNullable = false;
        try {
            return new JSONObject().put("name",name).put("type",type).put("isNullable",isNullable);
        } catch (JSONException e) {
            Log.e("ScoreKeyFragment","JSONException",e);
        }
        return new JSONObject();
    }
}
