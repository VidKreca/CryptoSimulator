package com.vidkreca.data;

/*
* Store all info about a trade that a user performed
* */
public class Trade {

    public String _id;
    public String uuid;
    public String type;
    public double fiat_value;
    public String fiat;
    public double crypto_value;
    public String crypto_symbol;


    public Trade(String uuid, String type, double fiat_value, String fiat, double crypto_value, String crypto_symbol) {
        this.uuid = uuid;
        this.type = type;
        this.fiat_value = fiat_value;
        this.fiat = fiat;
        this.crypto_value = crypto_value;
        this.crypto_symbol = crypto_symbol;
    }
}
