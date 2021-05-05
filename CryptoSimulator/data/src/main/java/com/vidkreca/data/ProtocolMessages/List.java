package com.vidkreca.data.ProtocolMessages;

import com.vidkreca.data.Cryptocurrency;

import java.util.Date;

public class List {
    public boolean success;
    public long timestamp;
    public String fiat;
    public Cryptocurrency[] currencies;
}
