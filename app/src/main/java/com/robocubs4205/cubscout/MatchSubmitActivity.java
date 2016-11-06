package com.robocubs4205.cubscout;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MatchSubmitActivity extends Activity {

    private DefenseRatingFragment defense_fragment = new DefenseRatingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_submit);
        getFragmentManager().beginTransaction().add(R.id.defense_spinner_frame, defense_fragment).commit();
        getFragmentManager().beginTransaction().hide(defense_fragment).commit();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        if (savedInstanceState.getBoolean("didDefense")) {
            ((CheckBox) findViewById(R.id.did_defense_checkbox)).setChecked(true);
            getFragmentManager().beginTransaction().show(defense_fragment).commit();
        }

        ((EditText)findViewById(R.id.high_goal_made_input_field)).setText(savedInstanceState.getString("highGoalsMade"));
        ((EditText)findViewById(R.id.high_goal_miss_input_field)).setText(savedInstanceState.getString("highGoalsMissed"));
        ((EditText)findViewById(R.id.low_goal_made_input_field)).setText(savedInstanceState.getString("lowGoalsMade"));
        ((EditText)findViewById(R.id.low_goal_miss_input_field)).setText(savedInstanceState.getString("lowGoalsMissed"));

        ((EditText)findViewById(R.id.portcullis_crossing_input_field)).setText(savedInstanceState.getString("Portcullis"));
        ((EditText)findViewById(R.id.cheval_crossing_input_field)).setText(savedInstanceState.getString("Cheval"));
        ((EditText)findViewById(R.id.moat_crossing_input_field)).setText(savedInstanceState.getString("Moat"));
        ((EditText)findViewById(R.id.rampart_crossing_input_field)).setText(savedInstanceState.getString("Ramparts"));
        ((EditText)findViewById(R.id.drawbridge_crossing_input_field)).setText(savedInstanceState.getString("Drawbridge"));
        ((EditText)findViewById(R.id.sally_port_crossing_input_field)).setText(savedInstanceState.getString("SallyPort"));
        ((EditText)findViewById(R.id.rough_terrain_crossing_input_field)).setText(savedInstanceState.getString("RoughTerrain"));
        ((EditText)findViewById(R.id.rock_wall_crossing_input_field)).setText(savedInstanceState.getString("RockWall"));
        ((EditText)findViewById(R.id.low_bar_crossing_input_field)).setText(savedInstanceState.getString("LowBar"));

        ((Spinner)findViewById(R.id.defense_rating_spinner)).setSelection(savedInstanceState.getInt("Defense"));
    }

    //must not call super.onSaveInstanceState to prevent
    //fragments from being duplicated on activity recreation
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("didDefense", ((CheckBox) findViewById(R.id.did_defense_checkbox)).isChecked());

        outState.putString("highGoalsMade",((EditText)findViewById(R.id.high_goal_made_input_field)).getText().toString());
        outState.putString("highGoalsMissed",((EditText)findViewById(R.id.high_goal_miss_input_field)).getText().toString());
        outState.putString("lowGoalsMade",((EditText)findViewById(R.id.low_goal_made_input_field)).getText().toString());
        outState.putString("lowGoalsMissed",((EditText)findViewById(R.id.low_goal_miss_input_field)).getText().toString());

        outState.putString("Portcullis",((EditText)findViewById(R.id.portcullis_crossing_input_field)).getText().toString());
        outState.putString("Cheval",((EditText)findViewById(R.id.cheval_crossing_input_field)).getText().toString());
        outState.putString("Moat",((EditText)findViewById(R.id.moat_crossing_input_field)).getText().toString());
        outState.putString("Ramparts",((EditText)findViewById(R.id.rampart_crossing_input_field)).getText().toString());
        outState.putString("Drawbridge",((EditText)findViewById(R.id.drawbridge_crossing_input_field)).getText().toString());
        outState.putString("SallyPort",((EditText)findViewById(R.id.sally_port_crossing_input_field)).getText().toString());
        outState.putString("RoughTerrain",((EditText)findViewById(R.id.rough_terrain_crossing_input_field)).getText().toString());
        outState.putString("RockWall",((EditText)findViewById(R.id.rock_wall_crossing_input_field)).getText().toString());
        outState.putString("LowBar",((EditText)findViewById(R.id.low_bar_crossing_input_field)).getText().toString());

        outState.putInt("Defense",((Spinner)findViewById(R.id.defense_rating_spinner)).getSelectedItemPosition());

    }

    public void onIncrementHighGoalMade(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.high_goal_made_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementHighGoalMade(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.high_goal_made_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementHighGoalMiss(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.high_goal_miss_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementHighGoalMiss(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.high_goal_miss_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementLowGoalMade(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.low_goal_made_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementLowGoalMade(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.low_goal_made_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementLowGoalMiss(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.low_goal_miss_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementLowGoalMiss(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.low_goal_miss_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementPortcullis(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.portcullis_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementPortcullis(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.portcullis_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementCheval(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.cheval_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementCheval(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.cheval_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementMoat(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.moat_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementMoat(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.moat_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementRamparts(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.rampart_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementRamparts(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.rampart_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementDrawbridge(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.drawbridge_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementDrawbridge(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.drawbridge_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementSallyPort(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.sally_port_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementSallyPort(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.sally_port_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementRoughTerrain(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.rough_terrain_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementRoughTerrain(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.rough_terrain_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementRockWall(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.rock_wall_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementRockWall(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.rock_wall_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onIncrementLowBar(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.low_bar_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) + 1;
        highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDecrementLowBar(View view) {
        EditText highGoalMadeText = (EditText) findViewById(R.id.low_bar_crossing_input_field);
        Integer nextNumber = Integer.parseInt(highGoalMadeText.getText().toString()) - 1;
        if (nextNumber >= 0)
            highGoalMadeText.setText(nextNumber.toString());
    }

    public void onDidDefenseChecked(View view)
    {
        if(((CheckBox)view).isChecked())
        {
            getFragmentManager().beginTransaction().show(defense_fragment).commit();
        }
        else
        {
           getFragmentManager().beginTransaction().hide(defense_fragment).commit();
        }
    }

    public void onSubmitButtonClick(View view)
    {
        MatchData data = new MatchData();
        if(((EditText)findViewById(R.id.team_number_input_field)).getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please enter a team number",Toast.LENGTH_LONG).show();
            return;
        }
        if(((EditText)findViewById(R.id.match_number_input_field)).getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please enter current the match number",Toast.LENGTH_LONG).show();
            return;
        }

        try {
            data.teamNumber = Integer.parseInt(((EditText)findViewById(R.id.team_number_input_field)).getText().toString());
            data.matchNumber = Integer.parseInt(((EditText)findViewById(R.id.match_number_input_field)).getText().toString());

            data.highGoal = Integer.parseInt(((EditText)findViewById(R.id.high_goal_made_input_field)).getText().toString());
            data.highMiss = Integer.parseInt(((EditText)findViewById(R.id.high_goal_miss_input_field)).getText().toString());
            data.lowGoal = Integer.parseInt(((EditText)findViewById(R.id.low_goal_made_input_field)).getText().toString());
            data.lowMiss = Integer.parseInt(((EditText)findViewById(R.id.low_goal_miss_input_field)).getText().toString());

            data.portcullis = Integer.parseInt(((EditText)findViewById(R.id.portcullis_crossing_input_field)).getText().toString());
            data.cheval = Integer.parseInt(((EditText)findViewById(R.id.cheval_crossing_input_field)).getText().toString());
            data.moat = Integer.parseInt(((EditText)findViewById(R.id.moat_crossing_input_field)).getText().toString());
            data.rampart = Integer.parseInt(((EditText)findViewById(R.id.rampart_crossing_input_field)).getText().toString());
            data.drawBridge = Integer.parseInt(((EditText)findViewById(R.id.drawbridge_crossing_input_field)).getText().toString());
            data.sallyPort = Integer.parseInt(((EditText)findViewById(R.id.sally_port_crossing_input_field)).getText().toString());
            data.roughTerrain = Integer.parseInt(((EditText)findViewById(R.id.rough_terrain_crossing_input_field)).getText().toString());
            data.rockWall = Integer.parseInt(((EditText)findViewById(R.id.rock_wall_crossing_input_field)).getText().toString());
            data.lowBar = Integer.parseInt(((EditText)findViewById(R.id.low_bar_crossing_input_field)).getText().toString());

            data.defense = ((Spinner)defense_fragment.getView().findViewById(R.id.defense_rating_spinner)).getSelectedItemPosition()+1;
            data.didDefense = ((CheckBox)findViewById(R.id.did_defense_checkbox)).isChecked();
        } catch (NumberFormatException e) {
            Log.e("bob","error",e);
            Toast.makeText(getApplicationContext(),"One or more fields are blank or do not contain numbers. Please make sure all fields have numbers. if you do not know what number to put, guess.",Toast.LENGTH_LONG).show();
        }

        new SubmitMatchTask().execute(data);
    }
    private class SubmitMatchTask extends AsyncTask<MatchData,Void,TaskResult>
    {
        @Override
        protected TaskResult doInBackground(MatchData... matchData)
        {
            for(MatchData data : matchData)
            {
                try
                {
                    Uri.Builder uri = new Uri.Builder();
                    uri.scheme("http").authority("data.robocubs4205.com").appendPath("submit")
                            .appendQueryParameter("robot", Integer.toString(data.teamNumber))
                            .appendQueryParameter("match", Integer.toString(data.matchNumber))
                            .appendQueryParameter("high", Integer.toString(data.highGoal))
                            .appendQueryParameter("highMiss", Integer.toString(data.highMiss))
                            .appendQueryParameter("low", Integer.toString(data.lowGoal))
                            .appendQueryParameter("lowMiss", Integer.toString(data.lowMiss))
                            .appendQueryParameter("portcullis", Integer.toString(data.portcullis))
                            .appendQueryParameter("cheval", Integer.toString(data.cheval))
                            .appendQueryParameter("ramparts", Integer.toString(data.rampart))
                            .appendQueryParameter("drawbridge", Integer.toString(data.drawBridge))
                            .appendQueryParameter("sallyPort", Integer.toString(data.sallyPort))
                            .appendQueryParameter("rockWall", Integer.toString(data.rockWall))
                            .appendQueryParameter("roughTerrain", Integer.toString(data.roughTerrain))
                            .appendQueryParameter("lowBar", Integer.toString(data.lowBar))
                            .appendQueryParameter("moat", Integer.toString(data.moat));
                    if(data.didDefense)
                    {
                        uri.appendQueryParameter("defense", Integer.toString(data.defense));
                    }
                    else
                    {
                        uri.appendQueryParameter("defense", "");
                    }
                    uri.build();
                    Log.d("bob","submission url: "+uri.toString());
                    URL url = new URL(uri.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    Log.d("bob",Integer.toString(connection.getResponseCode()));
                    if(connection.getResponseCode() != 200)
                    {
                        Log.e("bob","unable to reach server");
                        return new TaskResult(false,false);
                    }
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(connection.getInputStream());
                    NodeList errors = doc.getElementsByTagName("error");
                    for (int i = 0; i < errors.getLength(); i++)
                    {
                        Log.e("bob",((Element)errors.item(i)).getAttribute("type")+" "+errors.item(i).getTextContent());
                    }
                    if(errors.getLength()==0)
                        return new TaskResult(true,true);
                    else if(errors.getLength()==1)
                    {
                        Toast.makeText(getApplicationContext(),"There was an error of the following type: "+((Element)errors.item(1)).getAttribute("type")+". Please try again later",Toast.LENGTH_LONG).show();
                        return new TaskResult(true,false);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"There were errors processing your request. Please try again later.",Toast.LENGTH_LONG).show();
                        return new TaskResult(true,false);
                    }
                }
                catch (UnknownHostException e)
                {
                    Log.e("bob","unable to reach server");
                    return new TaskResult(false,false);
                }
                catch (Exception e)
                {
                    Log.e("bob","exception",e);
                }
            }
            return new TaskResult(true,false);
        }

        @Override
        protected void onPostExecute(TaskResult success) {
            if(success.isSuccessful)
            {
                finish();
            }
            else if(!success.isServerReachable)
            {
                Toast.makeText(getApplicationContext(),"Unable to connect to server. Please check your internet connection.",Toast.LENGTH_LONG).show();
            }
        }
    }
    private class TaskResult
    {
        Boolean isServerReachable;
        Boolean isSuccessful;
        TaskResult(Boolean isServerReachable, Boolean isSuccessful)
        {
            this.isServerReachable = isServerReachable;
            this.isSuccessful = isSuccessful;
        }
    }
    private class MatchData {
        public int teamNumber;
        public int matchNumber;
        public int highGoal;
        public int lowGoal;
        public int highMiss;
        public int lowMiss;
        public int portcullis;
        public int cheval;
        public int moat;
        public int rampart;
        public int drawBridge;
        public int sallyPort;
        public int rockWall;
        public int roughTerrain;
        public int lowBar;
        public int defense;
        public Boolean didDefense;
    }
}
