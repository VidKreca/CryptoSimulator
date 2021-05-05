package com.vidkreca.data;

import java.util.ArrayList;
import java.util.Arrays;

public class Store {
    private ArrayList<Cryptocurrency> cryptocurrencies;


    public Store() {
        cryptocurrencies = new ArrayList<>();
    }


    // Clear current cryptocurrencies and add new ones (with updated values)
    public void UpdateCryptocurrencies(Cryptocurrency[] c) {
        cryptocurrencies.clear();
        cryptocurrencies.addAll(Arrays.asList(c));
    }

    public Cryptocurrency GetAtIndex(int i) {
        if (i >= 0 && i < cryptocurrencies.size())
            return cryptocurrencies.get(i);
        else
            return null;
    }


    public int Size() {
        return cryptocurrencies.size();
    }


    @Override
    public String toString() {
        String str = "Cryptocurrencies: \n";
        for (Cryptocurrency c : cryptocurrencies) {
            str += c.toString() + "\n";
        }
        return str;
    }
}
