package com.robocubs4205.cubscout;

import com.robocubs4205.cubscout.net.GsonModule;
import com.robocubs4205.cubscout.net.NetModule;

/**
 * Created by trevor on 1/15/17.
 */

public class Application extends android.app.Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        applicationComponent =
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this))
                                          .gsonModule(new GsonModule())
                                          .netModule(new NetModule(this)).build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
