package com.robocubs4205.cubscout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by trevor on 10/26/16.
 */

public interface JSONSerializableInputFragment {
    JSONObject serialize() throws JSONException;
}
