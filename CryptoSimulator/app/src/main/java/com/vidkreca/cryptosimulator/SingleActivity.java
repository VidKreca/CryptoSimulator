package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.vidkreca.data.Cryptocurrency;
import com.vidkreca.data.ProtocolMessages.Single;


public class SingleActivity extends AppCompatActivity {

    private API api;
    private App app;
    private Cryptocurrency crypto;
    private String symbol_query;

    private TextView name;
    private TextView symbol;
    private TextView price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        api = new API(getApplicationContext());
        app = (App)getApplication();
        symbol_query = getIntent().getExtras().getString("symbol");

        // Assign UI elements
        name = findViewById(R.id.name);
        symbol = findViewById(R.id.symbol);
        price = findViewById(R.id.price);

        GetData();
    }


    private void GetData() {
        api.GetSingle(new VolleyCallBack() {
            @Override
            public void onSuccess(String json) {
                Single response = API.gson.fromJson(json, Single.class);
                crypto = response.GetCryptocurrency();

                // Assign retrieved values to UI elements
                name.setText(crypto.getName());
                symbol.setText(crypto.getSymbol());
                price.setText(Double.toString(crypto.getPrice()) + "€");    // TODO - consider currently selected FIAT currency
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getBaseContext(), "Could not retrieve cryptocurrency statistics.\n"+message, Toast.LENGTH_LONG).show();
            }
        }, symbol_query);
    }
}