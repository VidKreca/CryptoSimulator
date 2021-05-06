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
    }
}