package com.robocubs4205.cubscout.scorecardsubmit;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by trevor on 1/11/17.
 */

@Module
final class ScorecardSubmitModule {
    private final ScorecardSubmitView view;

    public ScorecardSubmitModule(final ScorecardSubmitView view) {

        this.view = view;
    }

    @Provides
    ScorecardSubmitView getView() {
        return view;
    }
}
