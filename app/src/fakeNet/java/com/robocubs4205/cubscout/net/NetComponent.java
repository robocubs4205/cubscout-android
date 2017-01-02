package com.robocubs4205.cubscout.net;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by trevor on 12/31/16.
 */

@Singleton
@Component(modules = {NetModule.class, GsonModule.class})
public interface NetComponent {
    CubscoutAPI cubscoutAPI();
}
