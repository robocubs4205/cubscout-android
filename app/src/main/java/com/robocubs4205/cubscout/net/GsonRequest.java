package com.robocubs4205.cubscout.net;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Created by trevor on 12/31/16.
 */

public class GsonRequest extends Request<JsonObject>{

    /** Default charset for JSON request. */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    @SuppressWarnings("WeakerAccess")
    @Inject
    public Gson mGson;
    private Response.Listener<JsonObject> mListener;
    private String mRequestBody;
    public GsonRequest(int method, String url, String requestBody, Response.Listener<JsonObject> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mRequestBody = requestBody;

        DaggerGsonComponent.create().gsonRequestInjectionHelper().inject(this);
    }

    @Override
    protected Response<JsonObject> parseNetworkResponse(NetworkResponse response) {
        try {
            JsonObject t = mGson.fromJson(
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers)),
                    JsonObject.class);
            return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException e) {
            Log.e("CubscoutNet","Exception when converting server response to object",e);
            return null;
        }
    }

    @Override
    protected void deliverResponse(JsonObject response) {
        mListener.onResponse(response);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public String getPostBodyContentType(){
        return getBodyContentType();
    }

    @Override
    public String getBodyContentType(){
        return PROTOCOL_CONTENT_TYPE;
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public byte[] getPostBody(){
        return getBody();
    }

    @Override
    public byte[] getBody(){
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        }
        catch (UnsupportedEncodingException e) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                          mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }

    public static class InjectionHelper {
        private final Gson mGson;

        @Inject
        public InjectionHelper(Gson gson) {
            mGson = gson;
        }

        void inject(GsonRequest gsonRequest) {
            gsonRequest.mGson = mGson;
        }
    }


}
