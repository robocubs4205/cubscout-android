package com.robocubs4205.cubscout.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.robocubs4205.cubscout.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ScorecardDesignerParagraphFragment extends ScorecardDesignerFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_scorecard_design_paragraph,container,false);
        view.findViewById(R.id.remove_button).setOnClickListener(new RemoveButtonOnClickListener());
        view.findViewById(R.id.up_button).setOnClickListener(new UpButtonOnClickListener());
        view.findViewById(R.id.down_button).setOnClickListener(new DownButtonOnClickListener());
        return view;
    }
    @SuppressWarnings("ConstantConditions")
    @Override
    public JSONObject serialize() throws JSONException {
        EditText titleText = (EditText)getView().findViewById(R.id.score_paragraph_field);
        return new JSONObject().put("section_type","paragraph").put("paragraph",titleText.getText().toString());
    }
}
