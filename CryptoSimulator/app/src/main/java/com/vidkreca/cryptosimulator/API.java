package com.vidkreca.cryptosimulator;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vidkreca.data.FiatCurrencies;

import org.json.JSONException;
import org.json.JSONObject;


public class API {
    // API related
    final static String url = "http://10.0.2.2:3000";
    private Context context;
    static Gson gson = new Gson();

    // Crypto related
    public FiatCurrencies fiat = FiatCurrencies.EUR;


    // Volley related
    RequestQueue queue = null;


    public API(Context context) {
        queue = Volley.newRequestQueue(context);
    }


    private void MakeGetRequest(String endpoint, final VolleyCallback callback) {
        endpoint = API.url.concat(endpoint);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, endpoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }
        );
        queue.add(stringRequest);
    }


    private void MakePostRequest(String endpoint, JSONObject data, final VolleyJsonCallback callback) {
        endpoint = API.url.concat(endpoint);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, endpoint, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException ex) {
                            Log.e("CryptoSimulator", ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }
        );
        queue.add(jsonRequest);
    }



    public void GetList(final VolleyCallback callback) {
        MakeGetRequest("/currency/list/", callback);
    }

    public void GetSingle(final VolleyCallback callback, String symbol) {
        String endpoint = "/currency/"+symbol;
        MakeGetRequest(endpoint, callback);
    }

    public void GetOptions(final VolleyCallback callback) {
        MakeGetRequest("/options/", callback);
    }

    public void GetUser(final VolleyCallback callback, String uuid) {
        String endpoint = "/user/" + uuid;
        MakeGetRequest(endpoint, callback);
    }

    public void CreateAccount(final VolleyJsonCallback callback, JSONObject data) {
        MakePostRequest("/user/", data, callback);
    }

    public void CreateTrade(final VolleyJsonCallback callback, JSONObject data) {
        MakePostRequest("/trade/", data, callback);
    }
}
