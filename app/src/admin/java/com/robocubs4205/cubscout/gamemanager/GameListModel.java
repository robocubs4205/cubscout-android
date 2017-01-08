package com.robocubs4205.cubscout.gamemanager;

import com.robocubs4205.cubscout.Game;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by trevor on 1/6/17.
 */

interface GameListModel {
    void addGame(Game game);

    void removeGame(int position);

    void changeGame(Game game, int position);

    Observable<Game> gameAddedObservable();

    Observable<Integer> gameRemovedObservable();

    Observable<Pair<Game, Integer>> gameChangedObservable();

    Observable<List<Game>> getAllGames();
}
