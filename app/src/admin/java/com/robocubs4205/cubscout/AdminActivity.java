package com.robocubs4205.cubscout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.robocubs4205.cubscout.gamemanager.GameManagerFragment;
import com.robocubs4205.cubscout.net.CubscoutAPI;
import com.robocubs4205.cubscout.net.DaggerNetComponent;
import com.robocubs4205.cubscout.net.NetModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by trevor on 12/30/16.
 */

public class AdminActivity extends AppCompatActivity {
    private final int EVENT_TAB_POSITION = 0;
    private final int GAME_TAB_POSITION = 1;
    private final int ROBOT_TAB_POSITION = 2;
    private final int NUM_TABS = 3;
    private final CompositeDisposable disposables = new CompositeDisposable();
    @Inject
    CubscoutAPI cubscoutAPI;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private GameManagerFragment gameManagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerAdminActivityComponent.builder().netComponent(
                DaggerNetComponent.builder().netModule(new NetModule(getApplicationContext()))
                                  .build()).build().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(com.robocubs4205.cubscout.R.layout.activity_admin);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case GAME_TAB_POSITION:
                        if(gameManagerFragment==null) gameManagerFragment = new GameManagerFragment();
                        return gameManagerFragment;
                    default:
                        return new Fragment();
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

    @OnClick(R.id.fab)
    public void onClick(){
        switch (viewPager.getCurrentItem()){
            case GAME_TAB_POSITION:

        }
    }
}
