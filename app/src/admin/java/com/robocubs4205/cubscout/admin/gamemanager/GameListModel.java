package com.robocubs4205.cubscout.admin.gamemanager;

import com.robocubs4205.cubscout.Game;

import io.reactivex.Observable;

/**
 * Created by trevor on 1/6/17.
 */

public interface GameListModel {
    void addGame(Game game);

    void removeGame(int position);

    Observable<Game> gameAddedObservable();

    Observable<Integer> gameRemovedObservable();
}
