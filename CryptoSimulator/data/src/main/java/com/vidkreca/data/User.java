package com.vidkreca.data;


import java.util.ArrayList;

public class User {
    private String id;
    private String uuid;

    private double balance;
    private ArrayList<Trade> trades;

    public User(String id, String uuid, double balance) {
        this.id = id;
        this.uuid = uuid;
        this.balance = balance;

        trades = new ArrayList<>();
    }


    public double getBalance() {
        return balance;
    }

    public void AddTrade(Trade t) {
        if (trades == null)
            trades = new ArrayList<>();

        trades.add(t);
        balance -= t.fiat_value;
    }
}