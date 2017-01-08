package com.robocubs4205.cubscout.gamemanager;

import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.net.CubscoutAPI;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by trevor on 1/2/17.
 */

@Module
class GameListModule {

    private final GameListView mView;
    private final List<Game> mGames;

    public GameListModule(GameListView view, List<Game> games ){
        mView = view;
        mGames = games;
    }

    @Provides
    GameListPresenter provideGameListPresenter(CubscoutAPI api){
        return new GameListPresenter(api,mView,mGames);
    }
}
