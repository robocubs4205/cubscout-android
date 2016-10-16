package com.robocubs4205.cubscout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void LaunchMatchSubmitActivity(View view)
    {
        Intent intent = new Intent(this,MatchSubmitActivity.class);
        startActivity(intent);
    }
    public void LaunchIndividualReportActivity(View view)
    {
        Intent intent = new Intent(this,IndividualReportActivity.class);
        startActivity(intent);
    }
    public void LaunchReportListActivity(View view)
    {
        Intent intent = new Intent(this,ReportListActivity.class);
        startActivity(intent);
    }
}
