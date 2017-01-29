package com.robocubs4205.cubscout.scorecardsubmit;

import dagger.Provides;

/**
 * Created by trevor on 1/11/17.
 */

@dagger.Module
final class Module {
    private final MVPView view;

    public Module(final MVPView view) {

        this.view = view;
    }

    @Provides
    MVPView getView() {
        return view;
    }
}
