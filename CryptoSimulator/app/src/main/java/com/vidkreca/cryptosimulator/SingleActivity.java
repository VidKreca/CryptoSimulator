package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vidkreca.data.Cryptocurrency;
import com.vidkreca.data.ProtocolMessages.Single;


public class SingleActivity extends AppCompatActivity implements TradeDialog.TradeDialogListener {

    private API api;
    private App app;
    private Cryptocurrency crypto;
    private String symbol_query;

    private TextView name;
    private TextView price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        app = (App)getApplication();
        api = app.GetApi();
        symbol_query = getIntent().getExtras().getString("symbol");

        // Assign UI elements
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);

        GetData();
    }


    private void GetData() {
        api.GetSingle(new VolleyCallback() {
            @Override
            public void onSuccess(String json) {
                Single response = API.gson.fromJson(json, Single.class);
                crypto = response.GetCryptocurrency();

                // Assign retrieved values to UI elements
                name.setText(crypto.getName());
                price.setText(Double.toString(crypto.getPrice()) + "€");    // TODO - consider currently selected FIAT currency
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getBaseContext(), "Could not retrieve cryptocurrency statistics.\n"+message, Toast.LENGTH_LONG).show();
            }
        }, symbol_query);
    }


    public void OnClickBuy(View v) {
        // Open trade dialog if the user has balance to use
        if (app.GetUser().getBalance() >= 1) {
            TradeDialog dialog = new TradeDialog((int)app.GetUser().getBalance());
            dialog.show(getSupportFragmentManager(), "Trade");
        } else {
            Toast.makeText(getBaseContext(), "You're too broke.", Toast.LENGTH_LONG).show();
        }
    }

    public void OnClickSell(View v) {
        Toast.makeText(getApplicationContext(), "Cannot sell yet. HODL", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void getResult(int amount) {
        // We get the fiat amount to purchase here, send POST request to server to complete trade
        app.CreateTrade(crypto.getSymbol(), "EUR", "buy", amount);
    }
}