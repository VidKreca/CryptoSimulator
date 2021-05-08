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

    // API url
    final static String url = "http://10.0.2.2:3000";

    static Gson gson = new Gson();

    // Volley
    RequestQueue queue = null;


    public API(Context context) {
        queue = Volley.newRequestQueue(context);
    }


    /**
     * Makes a HTTP GET request to the provided endpoint.
     * @param endpoint endpoint URL string to append to the API URL
     * @param callback callback object
     */
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


    /**
     * Make a HTTP POST request with a JSON body to the provided endpoint.
     * @param endpoint endpoint URL string to append to the API URL
     * @param data JSON request body
     * @param callback callback object
     */
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


    /**
     * Get list of all available cryptocurrencies.
     * @param callback callback object
     */
    public void GetList(final VolleyCallback callback) {
        MakeGetRequest("/currency/list/", callback);
    }

    /**
     * Get all info about a single cryptocurrency.
     * @param callback callback object
     * @param symbol symbol of the cryptocurrency
     */
    public void GetSingle(final VolleyCallback callback, String symbol) {
        String endpoint = "/currency/"+symbol;
        MakeGetRequest(endpoint, callback);
    }

    /**
     * Get app options: starting difficulties, more in the future.
     * @param callback callback object
     */
    public void GetOptions(final VolleyCallback callback) {
        MakeGetRequest("/options/", callback);
    }

    /**
     * Get user object using our UUID.
     * @param callback callback object
     * @param uuid app UUID string
     */
    public void GetUser(final VolleyCallback callback, String uuid) {
        String endpoint = "/user/" + uuid;
        MakeGetRequest(endpoint, callback);
    }

    /**
     * Create a new user account.
     * @param callback callback object
     * @param data JSON body with user info.
     */
    public void CreateAccount(final VolleyJsonCallback callback, JSONObject data) {
        MakePostRequest("/user/", data, callback);
    }

    /**
     * Create a new trade.
     * @param callback callback object
     * @param data JSON body with trade info.
     */
    public void CreateTrade(final VolleyJsonCallback callback, JSONObject data) {
        MakePostRequest("/trade/", data, callback);
    }
}
