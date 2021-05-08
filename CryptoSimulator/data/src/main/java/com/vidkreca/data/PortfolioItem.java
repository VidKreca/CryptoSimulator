package com.vidkreca.data;

public class PortfolioItem {

    public String crypto_symbol;
    public double crypto_amount;
    public double fiat_paid;
    public double fiat_worth;


    public double GetPercentageChange() {
        return (fiat_worth / fiat_paid) * 100;
    }
}
