package com.robocubs4205.cubscout.admin;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.robocubs4205.cubscout.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by trevor on 10/24/16.
 */

public class ScorecardDesignActivity extends Activity implements ScorecardDesignerFragmentListener {

    private final List<ScorecardDesignerFragment> scoredKeys = new ArrayList<>();
    private LinearLayout entries;

    ArrayList<Integer> gameYears = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard_design);
        entries = (LinearLayout)findViewById(R.id.entries);

        Spinner gameTypeSpinner = (Spinner)findViewById(R.id.game_type_spinner);
        ArrayAdapter<CharSequence> gameTypeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.game_types,android.R.layout.simple_spinner_item);
        gameTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameTypeSpinner.setAdapter(gameTypeSpinnerAdapter);

        Spinner gameYearSpinner = (Spinner)findViewById(R.id.game_year_spinner);
        ArrayList<CharSequence> yearArray = new ArrayList<>();
        for(int year = 1992; year <= Calendar.getInstance().get(Calendar.YEAR); year++)
        {
            yearArray.add(Integer.toString(year));
            gameYears.add(year);
        }

        ArrayAdapter<CharSequence> gameYearSpinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,yearArray);
        gameYearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameYearSpinner.setAdapter(gameYearSpinnerAdapter);
    }
    @Override
    public void onAttachFragment(Fragment fragment)
    {
        if(fragment instanceof ScorecardDesignerFragment)
            scoredKeys.add((ScorecardDesignerFragment) fragment);
    }
    @Override
    public void GameDesignerFragmentRemoveButtonClicked(ScorecardDesignerFragment fragment)
    {
        scoredKeys.remove(fragment);
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }
    @Override
    public void GameDesignerFragmentUpButtonClicked(ScorecardDesignerFragment fragment)
    {
        int currentIndex = entries.indexOfChild(fragment.getView());
        if(currentIndex>0)
        {
            entries.removeViewAt(currentIndex);
            entries.addView(fragment.getView(),currentIndex-1);
        }
    }
    @Override
    public void GameDesignerFragmentDownButtonClicked(ScorecardDesignerFragment fragment)
    {
        int currentIndex = entries.indexOfChild(fragment.getView());
        if(currentIndex<entries.getChildCount()-1)
        {
            entries.removeViewAt(currentIndex);
            entries.addView(fragment.getView(),currentIndex+1);
        }
    }
    public void onSubmitButtonClick(View view)
    {
        try {
            JSONObject output = new JSONObject();
            EditText nameField = (EditText)findViewById(R.id.game_name_field);
            Spinner gameTypeSpinner = (Spinner)findViewById(R.id.game_type_spinner);
            CharSequence[] gameTypes = getResources().getStringArray(R.array.game_types);
            Spinner gameYearSpinner= (Spinner)findViewById(R.id.game_year_spinner);
            output.put("game_name",nameField.getText().toString())
                  .put("game_type",gameTypes[gameTypeSpinner.getSelectedItemPosition()])
                  .put("game_year",gameYears.get(gameYearSpinner.getSelectedItemPosition()));
            JSONArray scorecardSectionArray = new JSONArray();
            for (ScorecardDesignerFragment fragment : scoredKeys) {
                scorecardSectionArray.put(fragment.serialize()
                                     .put("index",entries.indexOfChild(fragment.getView())));
            }
            output.put("sections", scorecardSectionArray);
            Log.d("ScorecardDesigner","serialization output: "+output.toString());
        }
        catch (JSONException e)
        {
            Log.e("ScorecardDesigner","exception while serializing",e);
        }
        Log.d("ScorecardDesignActivity",Integer.toString(scoredKeys.size()));
    }
    public void onAddKeyButtonClick(View view)
    {
        PopupMenu menu = new PopupMenu(this,view);
        menu.setOnMenuItemClickListener(new addKeyMenuItemClickListener());
        menu.inflate(R.menu.popup_admin_add_scored_key);
        menu.show();
    }

    private class addKeyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener
    {
        @Override
        public boolean onMenuItemClick(MenuItem item)
        {
            Log.d("ScorecardDesignActivity",getResources().getResourceEntryName(item.getItemId()));
            switch(item.getItemId())
            {
                case R.id.menu_item_add_score_field:
                    ScorecardDesignerScoreFieldFragment newScorecardDesignerScoreFieldFragment = new ScorecardDesignerScoreFieldFragment();
                    getFragmentManager().beginTransaction().add(R.id.entries, newScorecardDesignerScoreFieldFragment).commit();
                    return true;
                case R.id.menu_item_add_score_title:
                    ScorecardDesignerTitleFragment newScorecardDesignerTitleFragment = new ScorecardDesignerTitleFragment();
                    getFragmentManager().beginTransaction().add(R.id.entries, newScorecardDesignerTitleFragment).commit();
                    return true;
                case R.id.menu_item_add_score_paragraph:
                    ScorecardDesignerParagraphFragment newScorecardDesignerParagraphFragment = new ScorecardDesignerParagraphFragment();
                    getFragmentManager().beginTransaction().add(R.id.entries,newScorecardDesignerParagraphFragment).commit();
                default:
                    return false;
            }
        }
    }
}
