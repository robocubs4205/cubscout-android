package com.robocubs4205.cubscout.scorelist;

import com.robocubs4205.cubscout.ActivityScope;
import com.robocubs4205.cubscout.ApplicationComponent;
import com.robocubs4205.cubscout.net.NetModule;

import dagger.Component;

/**
 * Created by trevor on 1/14/17.
 */
@ActivityScope
@Component(modules = ScoreListModule.class,
           dependencies = ApplicationComponent.class)
interface ScoreListComponent {
    ScoreListPresenter presenter();
}
