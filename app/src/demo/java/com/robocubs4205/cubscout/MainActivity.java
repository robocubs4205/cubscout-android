package com.robocubs4205.cubscout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.robocubs4205.cubscout.scorecardsubmit.ScorecardSubmitActivity;
import com.robocubs4205.cubscout.scorelist.ScoreListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    public void LaunchMatchSubmitActivity(View view) {
        Intent intent = new Intent(this, ScorecardSubmitActivity.class);
        startActivity(intent);
    }

    public void LaunchReportListActivity(View view) {
        Intent intent = new Intent(this, ScoreListActivity.class);
        startActivity(intent);
    }
}
