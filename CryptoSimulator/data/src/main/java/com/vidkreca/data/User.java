package com.vidkreca.data;


import java.util.ArrayList;

public class User {
    private String id;
    private String uuid;

    private double balance;
    private ArrayList<Trade> trades;
    private PortfolioItem[] portfolio;

    public User(String id, String uuid, double balance) {
        this.id = id;
        this.uuid = uuid;
        this.balance = balance;

        trades = new ArrayList<>();
    }


    public double getBalance() {
        return balance;
    }
    public PortfolioItem[] GetPortfolio() {
        return portfolio;
    }


    public double GetPortfolioValue() {
        double sum = 0;
        if (portfolio != null)
            for (PortfolioItem p : portfolio) {
                sum += p.fiat_worth;
            }
        return sum;
    }

    public void AddTrade(Trade t) {
        if (trades == null)
            trades = new ArrayList<>();

        trades.add(t);
        balance -= t.fiat_value;
    }
}