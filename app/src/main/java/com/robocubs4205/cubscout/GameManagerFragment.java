package com.robocubs4205.cubscout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

/**
 * Created by trevor on 12/27/16.
 */

public class GameManagerFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_game_manager,container);
        SearchView gameSearch = (SearchView) view.findViewById(R.id.game_search_view);
        gameSearch.setIconifiedByDefault(false);
        return view;
    }
}
