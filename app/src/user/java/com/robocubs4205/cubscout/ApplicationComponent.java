package com.robocubs4205.cubscout;

import com.google.gson.Gson;
import com.robocubs4205.cubscout.net.CubscoutAPI;
import com.robocubs4205.cubscout.net.GsonModule;
import com.robocubs4205.cubscout.net.NetModule;

import dagger.Component;

/**
 * Created by trevor on 1/29/17.
 */
@ApplicationScope
@Component(modules = {NetModule.class, GsonModule.class, ApplicationModule.class})
public interface ApplicationComponent {
    CubscoutAPI api();

    Gson gson();

    Application application();
}
