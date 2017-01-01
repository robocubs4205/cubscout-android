package com.robocubs4205.cubscout.net;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;

/**
 * Created by trevor on 12/31/16.
 */

@Singleton
public class CubscoutAPI {

    private final OkHttpClient mClient;

    @Inject
    public CubscoutAPI(OkHttpClient client){
        mClient = client;
    }

}
