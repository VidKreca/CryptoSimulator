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

        // Default empty user to avoid null pointer exceptions
        user = new User("undefined_id", "undefined_uuid", 0);
    }


    public void refreshUser(RefreshCallback callback) {
        String uuid = this.getUUID();

        if (uuid == null)
            return;

        api.GetUser(new VolleyCallback() {
            @Override
            public void onSuccess(String json) {
                User response = API.gson.fromJson(json, User.class);
                setUser(response);

                callback.onRefresh();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), "Could not retrieve user.", Toast.LENGTH_LONG).show();
            }
        }, uuid);
    }




    /**
     * Return true if the user hasn't chosen a difficulty and created his account yet.
     * @return true if first start
     */
    public boolean isFirstStart() {
        return !sp.contains(UUID_SP_KEY);
    }

    public void setUUID(String uuid) {
        sp.edit().putString(UUID_SP_KEY, uuid).apply();     // Note - might want to use .commit here instead
    }
    public String getUUID() {
        return sp.getString(UUID_SP_KEY, null);
    }
    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
    public API getApi() { return api; }
    public Store getStore() {
        return store;
    }


    /**
     * Creates a new trade. If server response is successfull it also adds it as a trade to the User object.
     * @param symbol cryptocurrency symbol
     * @param fiat "EUR"
     * @param type "buy" or "sell"
     * @param amount amount of fiat to spend
     */
    public void createTrade(String symbol, String fiat, String type, double amount) {
        // Type should be "sell" or "buy"
        if (!type.equals("sell") && !type.equals("buy"))
            return;     // TODO - throw exception here

        // Create request object
        JSONObject data = new JSONObject();
        try {
            data.put("uuid", getUUID());
            data.put("type", type);
            data.put("fiat_value", amount);
            data.put("fiat", fiat);
            data.put("crypto_symbol", symbol);
        } catch (JSONException e) {
            return;     // TODO - throw exception here
        }

        api.createTrade(new VolleyJsonCallback() {
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

                String typeStr = (type == "buy") ? "Bought " : "Sold ";
                String text = typeStr + t.fiat_value + "€ of " + t.crypto_symbol;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }, data);
    }


    /**
     * Deletes our UUID which effectively deletes our user account and resets all progress.
     * This is not reversible easily.
     */
    public void resetUUID() {
        sp.edit().remove(UUID_SP_KEY).commit();
    }
}
