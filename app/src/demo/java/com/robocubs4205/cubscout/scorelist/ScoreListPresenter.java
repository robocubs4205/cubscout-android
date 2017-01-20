package com.robocubs4205.cubscout.scorelist;

import com.google.gson.Gson;
import com.robocubs4205.cubscout.Application;
import com.robocubs4205.cubscout.DemoDataProvider;
import com.robocubs4205.cubscout.Scorecard;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by trevor on 1/14/17.
 */

final class ScoreListPresenter {
    private final ScoreListView view;
    private final Application context;
    private final DemoDataProvider api;
    private final Gson gson;

    private Disposable getResultsDisposable;

    @Inject
    public ScoreListPresenter(final ScoreListView view, final Application context,
                              DemoDataProvider api,
                              Gson gson) {

        this.view = view;
        this.context = context;
        this.api = api;
        this.gson = gson;
    }

    public void initView() {
        Scorecard demoScorecard = api.getDemoScorecard();
        getResultsDisposable = api.getResults(demoScorecard, new String[]{"overall"})
                                  .subscribeOn(Schedulers.io()).observeOn(
                        AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Result>>() {
                    @Override
                    public void accept(List<Result> results) throws Exception {
                        view.LoadResults(results);
                    }
                });
        view.setScorecard(demoScorecard);
    }

    public void getResults(String[] orderBy) {
        if (getResultsDisposable != null) getResultsDisposable.dispose();
        getResultsDisposable = api.getResults(api.getDemoScorecard(), orderBy).subscribeOn(
                Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Result>>() {
            @Override
            public void accept(List<Result> results) throws Exception {
                view.LoadResults(results);
            }
        });
    }
}
