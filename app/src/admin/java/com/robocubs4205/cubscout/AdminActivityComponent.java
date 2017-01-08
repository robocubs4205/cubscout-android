package com.robocubs4205.cubscout;

import com.robocubs4205.cubscout.net.NetComponent;

import dagger.Component;

/**
 * Created by trevor on 12/31/16.
 */

@ActivityScope
@Component(dependencies = NetComponent.class)
interface AdminActivityComponent {
    void inject(AdminActivity activity);
}
