package com.robocubs4205.cubscout.admin;

import com.robocubs4205.cubscout.ActivityScope;
import com.robocubs4205.cubscout.net.NetComponent;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;

/**
 * Created by trevor on 12/31/16.
 */

@ActivityScope
@Component(dependencies = NetComponent.class)
interface AdminActivityComponent {
    void inject(AdminActivity activity);
}