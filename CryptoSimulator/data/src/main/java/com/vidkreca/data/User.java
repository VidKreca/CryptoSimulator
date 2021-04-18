package com.vidkreca.data;


import java.util.ArrayList;

public class User {
    private int id;
    private String username;
    private String email;

    private double balanceFiat;
    private ArrayList<Trade> trades;

    public User(int id, String username, String email) {
        // ...
    }
}