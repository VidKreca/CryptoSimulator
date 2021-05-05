package com.vidkreca.data;

import com.google.gson.Gson;

/*
* Store all information about a specific cryptocurrency that's available on our platform
* */
public class Cryptocurrency {
    private String name;
    private String symbol;
    public String fiat;

    private double price;
    public double high;
    public double low;
    public double volume;
    public double volume_30d;
    public double change;
    public double change_percentage;



    public Cryptocurrency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
    public Cryptocurrency(String name, String symbol, String fiat, double price, double high, double low, double volume, double volume_30d, double change, double change_percentage) {
        this.name = name;
        this.symbol = symbol;
        this.fiat = fiat;

        this.price = price;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.volume_30d = volume_30d;
        this.change = change;
        this.change_percentage = change_percentage;
    }


    @Override
    public String toString() {
        return symbol + ":" + name + ", price: " + price;
    }

    static Cryptocurrency FromJson(String json, Gson gson) {
        // Build an object from a json string that we retrieve from our API
        return gson.fromJson(json, Cryptocurrency.class);
    }



    /*
    * =============== Getters & Setters ===================
    * */

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
