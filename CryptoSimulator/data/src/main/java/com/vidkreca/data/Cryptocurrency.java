package com.vidkreca.data;

import com.google.gson.Gson;

/*
* Store all information about a specific cryptocurrency that's available on our platform
* */
public class Cryptocurrency {
    private String name;
    private String symbol;

    private double price;
    private double[] priceHistory;
    private double marketCap;
    private double volume24h;



    public Cryptocurrency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
    public Cryptocurrency(String name, String symbol, double price) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
    }


    @Override
    public String toString() {
        return symbol + ":" + name + ", price: " + price;
    }

    static Cryptocurrency FromJson(String json, Gson gson) {
        // Build an object from a json string that we retrieve from our API
        return gson.fromJson(json, Cryptocurrency.class);
    }
}
