package com.robocubs4205.cubscout.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.robocubs4205.cubscout.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by trevor on 10/26/16.
 */

public class ScorecardDesignerTitleFragment extends ScorecardDesignerFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_scorecard_design_title,container,false);
        view.findViewById(R.id.remove_button).setOnClickListener(new RemoveButtonOnClickListener());
        view.findViewById(R.id.up_button).setOnClickListener(new UpButtonOnClickListener());
        view.findViewById(R.id.down_button).setOnClickListener(new DownButtonOnClickListener());
        return view;
    }
    @Override
    public JSONObject serialize()
    {
        EditText titleText = (EditText)getView().findViewById(R.id.score_title_field);
        try {
            return new JSONObject().put("title",titleText.getText().toString());
        } catch (JSONException e) {
            Log.e("ScorecardDesigner","JSONException",e);
        }
        return new JSONObject();
    }
}
