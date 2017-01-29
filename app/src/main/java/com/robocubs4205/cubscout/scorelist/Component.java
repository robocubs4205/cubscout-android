package com.robocubs4205.cubscout.scorelist;

import com.robocubs4205.cubscout.ActivityScope;
import com.robocubs4205.cubscout.ApplicationComponent;

/**
 * Created by trevor on 1/14/17.
 */
@ActivityScope
@dagger.Component(modules = Module.class,
                  dependencies = ApplicationComponent.class)
interface Component {
    Presenter presenter();
}
