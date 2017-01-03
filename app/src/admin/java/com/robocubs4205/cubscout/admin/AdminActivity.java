package com.robocubs4205.cubscout.admin;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.R;
import com.robocubs4205.cubscout.admin.gamemanager.GameManagerFragment;
import com.robocubs4205.cubscout.net.DaggerNetComponent;
import com.robocubs4205.cubscout.net.Error;
import com.robocubs4205.cubscout.net.NetModule;
import com.robocubs4205.cubscout.net.CubscoutAPI;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by trevor on 12/30/16.
 */

public class AdminActivity extends AppCompatActivity {
    final int EVENT_TAB_POSITION = 0;
    final int GAME_TAB_POSITION = 1;
    final int ROBOT_TAB_POSITION = 2;
    final int NUM_TABS = 3;

    @Inject
    CubscoutAPI cubscoutAPI;

    CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerAdminActivityComponent.builder().netComponent(
                DaggerNetComponent.builder().netModule(new NetModule(getApplicationContext()))
                                  .build()).build().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(com.robocubs4205.cubscout.R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case GAME_TAB_POSITION:
                        return new GameManagerFragment();
                    default:
                        return new GameManagerFragment();
                }
            }

            @Override
            public int getCount() {
                return NUM_TABS;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case EVENT_TAB_POSITION:
                        return getResources().getString(R.string.event_tab_label_text);
                    case GAME_TAB_POSITION:
                        return getResources().getString(R.string.game_tab_label_text);
                    case ROBOT_TAB_POSITION:
                        return getResources().getString(R.string.robot_tab_label_text);
                    default:
                        return "";
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        disposables.add(cubscoutAPI.getCurrentEvents().subscribeOn(Schedulers.io())
                                   .observeOn(AndroidSchedulers.mainThread()).subscribe(
                        new Consumer<CubscoutAPI.GetEventsResponse>() {
                            @Override
                            public void accept(CubscoutAPI.GetEventsResponse getEventsResponse)
                                    throws Exception {
                                for (Error error : getEventsResponse.errors) {
                                    Log.e("AdminActivity",
                                          "Error when getting current events: " + error.toString());
                                }
                                Log.d("AdminActivity",
                                      "Events found: " + getEventsResponse.events.size());
                                for (Event event : getEventsResponse.events) {
                                    Log.d("AdminActivity", "Event found: " + event.toString());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("AdminActivity", "Exception when getting current events",
                                          throwable);
                            }
                        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setTitle("search games");
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("search games");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
