package com.robocubs4205.cubscout.admin.gamemanager;

import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.net.CubscoutAPI;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by trevor on 1/6/17.
 */
public class GameListModelImpl implements GameListModel {

    private BehaviorSubject<Game> gameAddedSubject = BehaviorSubject.create();
    private BehaviorSubject<Integer> gameRemovedSubject = BehaviorSubject.create();
    private BehaviorSubject<Pair<Game, Integer>> gameChangedSubject = BehaviorSubject.create();

    private CubscoutAPI mApi;

    @Inject
    public GameListModelImpl(CubscoutAPI api) {
        mApi = api;
    }

    @Override
    public void addGame(Game game) {
        Observable.just(game).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                new Consumer<Game>() {
                    @Override
                    public void accept(Game game) throws Exception {
                        gameAddedSubject.onNext(game);
                    }
                });
    }

    @Override
    public void removeGame(int position) {
        Observable.just(position).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                new Consumer<Integer>() {
                    @Override
                    public void accept(Integer position) throws Exception {
                        gameRemovedSubject.onNext(position);
                    }
                });
    }

    @Override
    public void changeGame(Game game, int position) {
        Observable<Pair<Game, Integer>> changeGameObservable = Observable.just(
                Pair.of(game, position)).subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
        changeGameObservable.subscribe(new Consumer<Pair<Game, Integer>>() {
            @Override
            public void accept(Pair<Game, Integer> gameIntegerPair) throws Exception {
                gameChangedSubject.onNext(gameIntegerPair);
            }
        });
        changeGameObservable.flatMap(new Function<Pair<Game, Integer>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Pair<Game, Integer> gameIntegerPair) throws Exception {
                throw new NotImplementedException("changeGame does not yet write to database");
            }
        });

    }

    @Override
    public Observable<Game> gameAddedObservable() {
        return gameAddedSubject.hide();
    }

    @Override
    public Observable<Integer> gameRemovedObservable() {
        return gameRemovedSubject.hide();
    }

    @Override
    public Observable<Pair<Game, Integer>> gameChangedObservable() {
        return gameChangedSubject.hide();
    }

    @Override
    public Observable<List<Game>> getAllGames() {
        throw new NotImplementedException("getAllGames not implemented");
    }


}
