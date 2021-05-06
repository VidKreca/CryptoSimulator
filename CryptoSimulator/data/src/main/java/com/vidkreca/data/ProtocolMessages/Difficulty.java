package com.vidkreca.data.ProtocolMessages;

public class Difficulty {
    public String difficulty;
    public int balance;

    @Override
    public String toString() {
        return difficulty + ", " + balance;
    }
}
