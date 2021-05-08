package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vidkreca.data.Cryptocurrency;
import com.vidkreca.data.PortfolioItem;
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


    /**
     * Get all data about this cryptocurrency.
     */
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


    /**
     * Open a trade dialog if the user has enough balance to make a trade.
     */
    public void OnClickBuy(View v) {
        // Open trade dialog if the user has balance to use
        if (app.GetUser().getBalance() >= 1) {
            TradeDialog dialog = new TradeDialog((int)app.GetUser().getBalance(), "buy");
            dialog.show(getSupportFragmentManager(), "Trade");
        } else {
            Toast.makeText(getBaseContext(), "You're too broke.", Toast.LENGTH_LONG).show();
        }
    }


    public void OnClickSell(View v) {
        PortfolioItem p = app.GetUser().GetPortfolioItem(crypto.getSymbol());

        if (p != null) {
            TradeDialog dialog = new TradeDialog(p.fiat_worth, "sell");
            dialog.show(getSupportFragmentManager(), "Trade");
        } else {
            // Shouldn't be reached...
            Toast.makeText(getApplicationContext(), "Cannot sell this cryptocurrency as you don't own any.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Handle result from the trade dialog. Only gets called when the user confirms the trade.
     * @param amount fiat amount to trade
     */
    @Override
    public void getResult(double amount, String type) {
        // We get the fiat amount to purchase here, send POST request to server to complete trade
        app.CreateTrade(crypto.getSymbol(), "EUR", type, amount);
    }
}