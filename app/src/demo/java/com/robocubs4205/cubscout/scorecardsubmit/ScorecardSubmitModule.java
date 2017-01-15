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
    private final Context context;

    public ScorecardSubmitModule(final ScorecardSubmitView view, final Context context) {

        this.view = view;
        this.context = context;
    }

    @Provides
    ScorecardSubmitView getView() {
        return view;
    }

    @Provides
    Context getContext() {
        return context;
    }
}
