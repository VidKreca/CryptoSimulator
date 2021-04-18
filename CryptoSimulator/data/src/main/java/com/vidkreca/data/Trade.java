package com.vidkreca.data;

/*
* Store all info about a trade that a user performed
* */
public class Trade {
    private Cryptocurrency crypto;
    private double priceFiat;

    public Trade(Cryptocurrency crypto, double priceFiat) {
        this.crypto = crypto;
        this.priceFiat = priceFiat;
    }
}
