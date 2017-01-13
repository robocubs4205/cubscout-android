package com.robocubs4205.cubscout.gamemanager;

import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.net.CubscoutAPI;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by trevor on 1/2/17.
 */

class GameListPresenter {

    private final CubscoutAPI mApi;
    private final List<Game> mGames;
    private final GameListView mView;

    private Disposable refreshDisposable;

    @Inject
    public GameListPresenter(CubscoutAPI api, GameListView view, List<Game> games) {
        mApi = api;
        mView = view;
        mGames = games;
        refreshList();
    }

    public void refreshList() {
        if (refreshDisposable != null && !refreshDisposable.isDisposed()) {
            refreshDisposable = mApi.getAllGames().subscribeOn(Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(
                    new Consumer<CubscoutAPI.GetGamesResponse>() {
                        @Override
                        public void accept(CubscoutAPI.GetGamesResponse getGamesResponse)
                                throws Exception {
                            mGames.clear();
                            mGames.addAll(getGamesResponse.games);
                            mView.notifyListChanged();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.showError("unable to retrieve gameEntities from server");
                        }
                    });
        }
    }
}
