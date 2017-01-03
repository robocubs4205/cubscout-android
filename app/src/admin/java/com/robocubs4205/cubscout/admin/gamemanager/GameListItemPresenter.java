package com.robocubs4205.cubscout.admin.gamemanager;

import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.net.CubscoutAPI;

import javax.inject.Inject;

/**
 * Created by trevor on 1/2/17.
 */

class GameListItemPresenter {
    private final GameListItemView mView;
    private final Game mGame;
    private final CubscoutAPI mApi;

    @Inject
    public GameListItemPresenter(GameListItemView view, Game game, CubscoutAPI api){
        mView = view;
        mGame = game;
        mApi = api;
    }

    public void setGameName(String newName){
        mGame.name = newName;
    }
    public void setGameYear(int newYear){
        mGame.year = newYear;
    }
    public void setGameType(String newType){
        mGame.type = newType;
    }
    public void saveChanges(){

    }
}
