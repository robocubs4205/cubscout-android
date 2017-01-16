package com.robocubs4205.cubscout.scorelist;

import android.content.Context;

import com.google.gson.Gson;
import com.robocubs4205.cubscout.Application;
import com.robocubs4205.cubscout.DemoDataProvider;
import com.robocubs4205.cubscout.net.CubscoutAPI;

import javax.inject.Inject;

/**
 * Created by trevor on 1/14/17.
 */

final class ScoreListPresenter {
    @Inject
    public ScoreListPresenter(final ScoreListView view, final Application context,
                              DemoDataProvider api,
                              Gson gson) {

    }

    public void initView() {

    }
}
