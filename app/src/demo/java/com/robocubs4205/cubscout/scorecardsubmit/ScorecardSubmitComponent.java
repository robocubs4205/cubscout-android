package com.robocubs4205.cubscout.scorecardsubmit;

import com.robocubs4205.cubscout.ActivityScope;
import com.robocubs4205.cubscout.ApplicationComponent;

import dagger.Component;

/**
 * Created by trevor on 1/11/17.
 */
@ActivityScope
@Component(modules = {ScorecardSubmitModule.class},
           dependencies = ApplicationComponent.class)
interface ScorecardSubmitComponent {
    ScorecardSubmitPresenter presenter();
}
