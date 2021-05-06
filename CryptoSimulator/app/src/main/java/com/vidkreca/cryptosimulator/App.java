package com.vidkreca.cryptosimulator;

import android.app.Application;

import com.vidkreca.data.Store;
import com.vidkreca.data.User;

public class App extends Application {

    public final String SHARED_PREFERENCES_TAG = "CryptoSimulator";
    public final String UUID_SP_KEY = "uuid";

    private API api;
    private Store store;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();

        api = new API(getApplicationContext());
        store = new Store();
    }



    public boolean IsFirstStart() {
        return !getSharedPreferences(SHARED_PREFERENCES_TAG, MODE_PRIVATE).contains(UUID_SP_KEY);
    }


    public API GetApi() { return api; }
    public Store GetStore() {
        return store;
    }
}
