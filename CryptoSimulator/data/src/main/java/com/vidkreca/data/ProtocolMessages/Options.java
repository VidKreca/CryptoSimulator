package com.vidkreca.data.ProtocolMessages;

import java.util.Arrays;

public class Options {
    public boolean success;
    public Difficulty[] startingDifficulties;


    @Override
    public String toString() {
        String str = "Options: \n";
        for (Difficulty d : startingDifficulties) {
            str += d.toString() + "\n";
        }
        return str;
    }
}
