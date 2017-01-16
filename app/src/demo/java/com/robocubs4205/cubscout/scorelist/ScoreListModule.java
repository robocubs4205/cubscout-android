package com.robocubs4205.cubscout.scorelist;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by trevor on 1/14/17.
 */

@Module
final class ScoreListModule {
    private final ScoreListView view;

    public ScoreListModule(final ScoreListView view) {

        this.view = view;
    }

    @Provides
    ScoreListView provideScoreListView() {
        return view;
    }
}
