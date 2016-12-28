package com.robocubs4205.cubscout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.robocubs4205.cubscout.admin.GameManagerFragment;
import com.robocubs4205.cubscout.admin.ScorecardDesignActivity;

/**
 * Created by trevor on 12/28/16.
 */

public class MainActivity extends AppCompatActivity {
    MainFragment mainFragment;
    GameManagerFragment gameManagerFragment;
    final String mainFragmentTag = "mainFragment";
    final String gameManagerFragmentTag = "gameManagerFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
        mainFragment = (MainFragment) getFragmentManager().findFragmentByTag(mainFragmentTag);
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            getFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, mainFragment, mainFragmentTag)
                                .commit();

        }
        gameManagerFragment = (GameManagerFragment) getFragmentManager()
                .findFragmentByTag(gameManagerFragmentTag);
        if (gameManagerFragment == null) {
            gameManagerFragment = new GameManagerFragment();
            getFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, gameManagerFragment,
                                     gameManagerFragmentTag)
                                .hide(gameManagerFragment)
                                .commit();

        }
    }

    @SuppressWarnings("UnusedParameters")
    public void LaunchMatchSubmitActivity(View view) {
        Intent intent = new Intent(this, MatchSubmitActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("UnusedParameters")
    public void LaunchIndividualReportActivity(View view) {
        Intent intent = new Intent(this, IndividualReportActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("UnusedParameters")
    public void LaunchReportListActivity(View view) {
        Intent intent = new Intent(this, ReportListActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("UnusedParameters")
    public void LaunchGameDesignActivity(View view) {
        Intent intent = new Intent(this, ScorecardDesignActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("UnusedParameters")
    public void LaunchGameManager(View view) {
        getFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in,
                                                 android.R.animator.fade_out,
                                                 android.R.animator.fade_in,
                                                 android.R.animator.fade_out).hide(mainFragment)
                            .show(gameManagerFragment).addToBackStack(null)
                            .commit();
    }
}
