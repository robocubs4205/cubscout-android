package com.robocubs4205.cubscout.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.robocubs4205.cubscout.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindString;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;

/**
 * Created by trevor on 12/31/16.
 */

@Singleton
public class StagingCubscoutAPI implements CubscoutAPI {

    private final OkHttpClient mClient;
    private final RequestQueue mRequestQueue;
    private final Gson mGson;

    @BindString(R.string.get_current_events_url)
    String getCurrentEventsURL;
    @BindString(R.string.get_games_url)
    String getGamesURL;

    @Inject
    public StagingCubscoutAPI(OkHttpClient client, RequestQueue requestQueue, Context context, Gson gson) {
        mClient = client;
        mRequestQueue = requestQueue;
        mGson = gson;

        //noinspection unchecked
        new StagingCubscoutAPI_ViewBinding(this,context);
    }

    public Observable<GetEventsResponse> getCurrentEvents() {
        RequestFuture<JsonObject> future = RequestFuture.newFuture();
        GsonRequest request = new GsonRequest(Request.Method.GET,
                                              getCurrentEventsURL,
                                              null, future, future);
        mRequestQueue.add(request);
        return Observable.fromFuture(future).map(new Function<JsonObject, GetEventsResponse>() {
            @Override
            public GetEventsResponse apply(JsonObject jsonObject) throws Exception {
                return mGson.fromJson(jsonObject,GetEventsResponse.class);
            }
        });
    }

    @Override
    public Observable<GetGamesResponse> getAllGames() {
        RequestFuture<JsonObject> future = RequestFuture.newFuture();
        GsonRequest request = new GsonRequest(Request.Method.GET,
                                              getGamesURL,
                                              null, future, future);
        mRequestQueue.add(request);
        return Observable.fromFuture(future).map(new Function<JsonObject, GetGamesResponse>() {
            @Override
            public GetGamesResponse apply(JsonObject jsonObject) throws Exception {
                return mGson.fromJson(jsonObject, GetGamesResponse.class);
            }
        });
    }

}
