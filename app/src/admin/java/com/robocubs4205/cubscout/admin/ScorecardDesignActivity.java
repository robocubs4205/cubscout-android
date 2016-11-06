package com.robocubs4205.cubscout.admin;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.robocubs4205.cubscout.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by trevor on 10/24/16.
 */

public class ScorecardDesignActivity extends Activity implements ScorecardDesignerFragmentListener {

    private final List<ScorecardDesignerFragment> scoredKeys = new ArrayList<>();
    private LinearLayout entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard_design);
        entries = (LinearLayout)findViewById(R.id.entries);
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
        for(ScorecardDesignerFragment fragment : scoredKeys)
        {

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
                case R.id.menu_item_add_scored_key:
                    ScorecardDesignerScoredValueFragment newScorecardDesignerScoredValueFragment = new ScorecardDesignerScoredValueFragment();
                    getFragmentManager().beginTransaction().add(R.id.entries, newScorecardDesignerScoredValueFragment).commit();
                    return true;
                case R.id.menu_item_add_score_title:
                    ScorecardDesignerTitleFragment newScorecardDesignerTitleFragment = new ScorecardDesignerTitleFragment();
                    getFragmentManager().beginTransaction().add(R.id.entries, newScorecardDesignerTitleFragment).commit();
                    return true;
                default:
                    return false;
            }
        }
    }
}
