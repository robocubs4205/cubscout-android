package com.robocubs4205.cubscout.admin.gamemanager;

import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.net.CubscoutAPI;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by trevor on 1/2/17.
 */

class GameListPresenter {

    private final CubscoutAPI mApi;
    private final List<Game> mGames = new ArrayList<>();
    private final GameListView mView;

    @Inject
    public GameListPresenter(CubscoutAPI api, GameListView view){
        mApi = api;
        mView = view;

    }

    void refreshList(){

    }
}
