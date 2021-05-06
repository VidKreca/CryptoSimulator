package com.vidkreca.cryptosimulator;

import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyJsonCallback {
    void onSuccess(JSONObject json) throws JSONException;
    void onError(String message);
}
