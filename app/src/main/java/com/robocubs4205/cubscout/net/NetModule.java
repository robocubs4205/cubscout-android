package com.robocubs4205.cubscout.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.circle.android.api.OkHttp3Stack;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by trevor on 12/31/16.
 */

@Module
class NetModule {
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(){
        OkHttpClient httpClient = new OkHttpClient();
        return httpClient;
    }

    @Provides
    @Singleton
    RequestQueue provideVoleyRequestQueue(Context context, OkHttp3Stack stack){
        return Volley.newRequestQueue(context,stack);
    }
}
