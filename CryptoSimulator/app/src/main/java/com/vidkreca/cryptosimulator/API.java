package com.vidkreca.cryptosimulator;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vidkreca.data.FiatCurrencies;





public class API {
    // API related
    final static String baseUrl = "http://10.0.2.2:3000";
    final static String url = baseUrl + "/api/";
    private Context context;
    static Gson gson = new Gson();

    // Crypto related
    public FiatCurrencies fiat = FiatCurrencies.EUR;


    // Volley related
    RequestQueue queue = null;


    public API(Context context) {
        queue = Volley.newRequestQueue(context);
    }


    private void MakeRequest(String endpoint, final VolleyCallBack callback) {
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
                        callback.onError("Error response: "+error.getMessage());
                    }
                }
        );
        queue.add(stringRequest);
    }



    public void GetList(final VolleyCallBack callback) {
        MakeRequest("currencies/list/", callback);
    }

    public void GetSingle(final VolleyCallBack callback, String symbol) {
        String endpoint = "currencies/"+symbol;
        MakeRequest(endpoint, callback);
    }
}
