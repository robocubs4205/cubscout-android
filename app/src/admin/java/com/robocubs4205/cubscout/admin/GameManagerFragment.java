package com.robocubs4205.cubscout.admin;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.robocubs4205.cubscout.R;

/**
 * Created by trevor on 12/27/16.
 */

public class GameManagerFragment extends Fragment {
    SearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_game_manager,parent,false);
        searchView = (SearchView)view.findViewById(R.id.game_search_view);
        searchView.setQueryHint("Search Games");
        searchView.setIconifiedByDefault(false);
        return view;
    }
}
