package com.robocubs4205.cubscout;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;

import com.robocubs4205.cubscout.admin.AdminActivity;

/**
 * Created by trevor on 12/28/16.
 */

public class MainActivity extends AppCompatActivity {
    MainFragment mainFragment;
    final String mainFragmentTag = "mainFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mainFragment = (MainFragment) getFragmentManager().findFragmentByTag(mainFragmentTag);
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            getFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, mainFragment, mainFragmentTag)
                                .commit();
        }
    }

    @Override
    public void onNewIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setTitle("search games");
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("search games");
        return super.onCreateOptionsMenu(menu);
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
    public void LaunchAdminActivity(View view) {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}
