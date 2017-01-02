package com.robocubs4205.cubscout.net;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by trevor on 1/1/17.
 */

@Module
public class NetModule {
    public NetModule(Context context){

    }

    @Provides
    public CubscoutAPI provideCubscoutApi(){
        return new StubCubscoutApi();
    }
}
