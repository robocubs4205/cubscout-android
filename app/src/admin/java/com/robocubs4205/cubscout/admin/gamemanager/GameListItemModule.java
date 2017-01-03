package com.robocubs4205.cubscout.admin.gamemanager;

import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.net.CubscoutAPI;

import dagger.Module;
import dagger.Provides;

/**
 * Created by trevor on 1/2/17.
 */

@Module
public class GameListItemModule {
    private final GameListItemView mView;
    private final Game mGame;

    public GameListItemModule(GameListItemView view, Game game){
        mView = view;
        mGame = game;
    }

    @Provides
    GameListItemPresenter provideListItemPresenter(CubscoutAPI api){
        return  new GameListItemPresenter(mView, mGame, api);
    }
}
