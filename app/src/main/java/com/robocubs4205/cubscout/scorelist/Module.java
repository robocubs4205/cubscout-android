package com.robocubs4205.cubscout.scorelist;

import dagger.Provides;

/**
 * Created by trevor on 1/14/17.
 */

@dagger.Module
final class Module {
    private final MVPView view;

    public Module(final MVPView view) {

        this.view = view;
    }

    @Provides
    MVPView provideScoreListView() {
        return view;
    }
}
