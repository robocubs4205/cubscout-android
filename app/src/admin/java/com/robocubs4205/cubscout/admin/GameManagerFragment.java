package com.robocubs4205.cubscout.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.R;

import java.util.List;

/**
 * Created by trevor on 12/27/16.
 */

public class GameManagerFragment extends Fragment {
    RecyclerView gameListView;
    private List<Game> games;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_game_manager,parent,false);
        gameListView = (RecyclerView) view.findViewById(R.id.game_list);
        gameListView.setAdapter(new GameListAdapter(games));
        return view;
    }

    private class GameListAdapter extends RecyclerView.Adapter<BaseViewHolder>{

        List<Game> mGames;

        public GameListAdapter(List<Game> games){
            super();
            mGames = games;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder,
                                     int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
    private static class BaseViewHolder extends RecyclerView.ViewHolder{
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }
}
