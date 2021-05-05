package com.vidkreca.data.ProtocolMessages;

import com.vidkreca.data.Cryptocurrency;

public class Single {
    public boolean success;
    public long timestamp;
    public String fiat;
    public double price;
    public String symbol;
    public String name;


    public Cryptocurrency GetCryptocurrency() {
        return new Cryptocurrency(name, symbol, price);
    }
}
