package com.robocubs4205.cubscout.scorecardsubmit;

import com.robocubs4205.cubscout.ActivityScope;
import com.robocubs4205.cubscout.net.GsonComponent;
import com.robocubs4205.cubscout.net.NetComponent;
import com.robocubs4205.cubscout.net.NetModule;

import dagger.Component;

/**
 * Created by trevor on 1/11/17.
 */
@ActivityScope
@Component(modules = {ScorecardSubmitModule.class, NetModule.class},
           dependencies = {GsonComponent.class})
interface ScorecardSubmitComponent {
    ScorecardSubmitPresenter presenter();
}
