package com.vidkreca.cryptosimulator;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vidkreca.data.FiatCurrencies;


public class API {
    // API related
    //final static String url = "http://localhost:3000/api/";
    final static String url = "http://10.0.2.2:3000/api/";
    private Context context;

    // Crypto related
    public FiatCurrencies fiat = FiatCurrencies.EUR;


    // Volley related
    RequestQueue queue = null;


    public API(Context context) {
        queue = Volley.newRequestQueue(context);

    }



    public void GetList(final VolleyCallBack callback) {
        String endpoint = API.url.concat("currencies/list/");
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
                        Log.e("Error response", endpoint);
                        callback.onError("Error response at endpoint: "+endpoint+"\n"+error.getMessage());
                    }
                }
        );

        queue.add(stringRequest);
    }
}
