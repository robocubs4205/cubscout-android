package com.robocubs4205.cubscout.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.robocubs4205.cubscout.IndividualReportActivity;
import com.robocubs4205.cubscout.MatchSubmitActivity;
import com.robocubs4205.cubscout.R;
import com.robocubs4205.cubscout.ReportListActivity;

/**
 * Created by trevor on 10/3/16.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
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
    public void LaunchGameDesignActivity(View view)
    {
        Intent intent = new Intent(this,GameDesignActivity.class);
        startActivity(intent);
    }
}
