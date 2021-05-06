package com.vidkreca.cryptosimulator;

import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(String json);
    void onError(String message);
}


