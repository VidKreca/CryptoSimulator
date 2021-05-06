package com.vidkreca.cryptosimulator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.vidkreca.data.Store;
import com.vidkreca.data.User;

import java.util.UUID;

public class App extends Application {

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
    }



    public boolean IsFirstStart() {
        return !sp.contains(UUID_SP_KEY);
    }

    public void SetUUID(String uuid) {
        sp.edit().putString(UUID_SP_KEY, uuid).apply();     // Note - might want to use .commit here instead
    }

    public void SetUser(User user) {
        this.user = user;
    }


    public API GetApi() { return api; }
    public Store GetStore() {
        return store;
    }
}
