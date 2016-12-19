package com.robocubs4205.cubscout;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONSerializableInputFragment {
    JSONObject serialize() throws JSONException;
}
