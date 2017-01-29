package com.robocubs4205.cubscout.scorelist;

import com.google.gson.Gson;
import com.robocubs4205.cubscout.Application;
import com.robocubs4205.cubscout.Scorecard;
import com.robocubs4205.cubscout.net.FakeCubscoutApi;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by trevor on 1/14/17.
 */

final class Presenter {
    private final MVPView view;
    private final Application context;
    private final FakeCubscoutApi api;
    private final Gson gson;

    private Disposable getResultsDisposable;

    @Inject
    public Presenter(final MVPView view, final Application context,
                     FakeCubscoutApi api,
                     Gson gson) {

        this.view = view;
        this.context = context;
        this.api = api;
        this.gson = gson;
        Scorecard demoScorecard = FakeCubscoutApi.getDemoScorecard();
        getResultsDisposable = api.getResults(demoScorecard, new String[]{"overall"})
                                  .subscribeOn(Schedulers.io()).observeOn(
                        AndroidSchedulers.mainThread()).subscribe(view::LoadResults);
        view.setScorecard(demoScorecard);
    }

    public void getResults(String[] orderBy) {
        ArrayList<String> orderByList = new ArrayList<>(Arrays.asList(orderBy));
        orderByList.add("overall");
        orderBy = orderByList.toArray(orderBy);
        if (getResultsDisposable != null) getResultsDisposable.dispose();
        getResultsDisposable = api.getResults(FakeCubscoutApi.getDemoScorecard(), orderBy)
                                  .subscribeOn(
                Schedulers.io()).observeOn(
                        AndroidSchedulers.mainThread()).subscribe(view::LoadResults);
    }
}
