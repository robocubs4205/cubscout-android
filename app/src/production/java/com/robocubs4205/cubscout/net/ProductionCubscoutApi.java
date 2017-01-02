package com.robocubs4205.cubscout.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.robocubs4205.cubscout.R;

import javax.inject.Inject;

import butterknife.BindString;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by trevor on 1/1/17.
 */
public class ProductionCubscoutApi implements CubscoutAPI {

    private final RequestQueue mRequestQueue;
    private final Gson mGson;
    @BindString(R.string.get_current_events_url) String getCurrentEventsURL;

    @Inject
    public ProductionCubscoutApi(RequestQueue requestQueue, Gson gson, Context context){
        mRequestQueue = requestQueue;
        mGson = gson;
        //noinspection unchecked
        new ProductionCubscoutApi_ViewBinding(this,context);
    }

    @Override
    public Observable<GetEventsResponse> getCurrentEvents() {
        RequestFuture<JsonObject> future = RequestFuture.newFuture();
        mRequestQueue.add(new GsonRequest(Request.Method.GET,getCurrentEventsURL,null,future,future));
        return Observable.fromFuture(future).map(new Function<JsonObject, GetEventsResponse>() {
            @Override
            public GetEventsResponse apply(JsonObject jsonObject) throws Exception {
                return mGson.fromJson(jsonObject,GetEventsResponse.class);
            }
        });
    }
}
