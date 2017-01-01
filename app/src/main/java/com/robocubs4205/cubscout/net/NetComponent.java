package com.robocubs4205.cubscout.net;

import com.circle.android.api.OkHttp3Stack;
import com.circle.android.api.OkHttp3Stack_Factory;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by trevor on 12/31/16.
 */

@Singleton
@Component(modules = NetModule.class)
public interface NetComponent {
    OkHttp3Stack okHttp3Stack();
    CubscoutAPI cubscoutAPI();
}
