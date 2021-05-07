package com.vidkreca.cryptosimulator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.vidkreca.data.Store;
import com.vidkreca.data.Trade;
import com.vidkreca.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class App extends Application {

    public final String TAG = "CryptoSimulator";
    public final String SHARED_PREFERENCES_TAG = "CryptoSimulator";
    public final String UUID_SP_KEY = "uuid";

    private API api;
    private Store store;
    private User user;
    private SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        api = new API(getApplicationContext());
        store = new Store();
        sp = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);

        // Default empty user
        user = new User("undefined_id", "undefined_uuid", 0);
    }



    public boolean IsFirstStart() {
        return !sp.contains(UUID_SP_KEY);
    }

    public void SetUUID(String uuid) {
        sp.edit().putString(UUID_SP_KEY, uuid).apply();     // Note - might want to use .commit here instead
    }
    public String GetUUID() {
        return sp.getString(UUID_SP_KEY, null);
    }
    public void SetUser(User user) {
        this.user = user;
    }
    public User GetUser() {
        return user;
    }
    public API GetApi() { return api; }
    public Store GetStore() {
        return store;
    }


    public void CreateTrade(String symbol, String fiat, String type, double amount) {
        // Type should be "sell" or "buy"
        if (!type.equals("sell") && !type.equals("buy"))
            return;     // TODO - throw exception here

        // Create request object
        JSONObject data = new JSONObject();
        try {
            data.put("uuid", GetUUID());
            data.put("type", type);
            data.put("fiat_value", amount);
            data.put("fiat", fiat);
            data.put("crypto_symbol", symbol);
        } catch (JSONException e) {
            return;     // TODO - throw exception here
        }

        api.CreateTrade(new VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject json) throws JSONException {
                Trade t = new Trade(
                        json.getString("uuid"),
                        json.getString("type"),
                        json.getDouble("fiat_value"),
                        json.getString("fiat"),
                        json.getDouble("crypto_value"),
                        json.getString("crypto_symbol")
                );

                user.AddTrade(t);

                Toast.makeText(getApplicationContext(), "Added new trade", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }, data);
    }




    /*
    * ===================== DEBUGGING ===================
    * */
    public void ResetUUID() {
        sp.edit().remove(UUID_SP_KEY).commit();
    }
}
