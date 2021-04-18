package com.vidkreca.data;

/*
* Store all info about a trade that a user performed
* */
public class Trade {
    private Cryptocurrency crypto;
    private double priceFiat;
    private FiatCurrencies fiatCurrency;

    public Trade(Cryptocurrency crypto, double priceFiat, FiatCurrencies fiatCurrency) {
        this.crypto = crypto;
        this.priceFiat = priceFiat;
        this.fiatCurrency = fiatCurrency;
    }
}
