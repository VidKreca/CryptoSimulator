package com.vidkreca.data.ProtocolMessages;

import com.vidkreca.data.Cryptocurrency;

public class Single {
    public boolean success;
    public long timestamp;
    public String fiat;
    public String symbol;
    public String name;
    public double price;
    public double high;
    public double low;
    public double volume;
    public double volume_30d;
    public double change;
    public double change_percentage;


    public Cryptocurrency GetCryptocurrency() {
        return new Cryptocurrency(name, symbol, fiat, price, high, low, volume, volume_30d, change, change_percentage);
    }
}
