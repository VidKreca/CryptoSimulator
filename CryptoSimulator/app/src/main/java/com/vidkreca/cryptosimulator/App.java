package com.vidkreca.cryptosimulator;

import android.app.Application;

import com.vidkreca.data.Store;

public class App extends Application {

    private Store store;

    @Override
    public void onCreate() {
        super.onCreate();

        store = new Store();
    }


    public Store GetStore() {
        return store;
    }
}
