package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vidkreca.data.Cryptocurrency;
import com.vidkreca.data.PortfolioItem;


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
        api = app.getApi();
        symbol_query = getIntent().getExtras().getString("symbol");

        // Assign UI elements
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);

        // Fake chart image
        String imageUrl = API.url + "/img/chart.png";
        Picasso.with(getApplicationContext()).load(imageUrl)
                .centerCrop()
                .fit()
                .into((ImageView) findViewById(R.id.imageView));

        getData();
    }


    /**
     * Get all data about this cryptocurrency.
     */
    private void getData() {
        api.getSingle(new VolleyCallback() {
            @Override
            public void onSuccess(String json) {
                crypto = API.gson.fromJson(json, Cryptocurrency.class);

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
    public void onClickBuy(View v) {
        // Open trade dialog if the user has balance to use
        if (app.getUser().getBalance() >= 1) {
            TradeDialog dialog = new TradeDialog((int)app.getUser().getBalance(), "buy", getApplicationContext());
            dialog.show(getSupportFragmentManager(), "Trade");
        } else {
            Toast.makeText(getBaseContext(), "You're too broke.", Toast.LENGTH_LONG).show();
        }
    }


    public void onClickSell(View v) {
        PortfolioItem p = app.getUser().GetPortfolioItem(crypto.getSymbol());

        if (p != null) {
            TradeDialog dialog = new TradeDialog(p.fiat_worth, "sell", getApplicationContext());
            dialog.show(getSupportFragmentManager(), "Trade");
        } else {
            // Shouldn't be reached...
            Toast.makeText(getApplicationContext(), "Cannot sell this cryptocurrency as you don't own any.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Start map activity to show the crypto's location on map
     */
    public void onClickMap(View v) {
        Intent i = new Intent(getApplicationContext(), MapActivity.class);
        i.putExtra("latitude", crypto.latitude);
        i.putExtra("longitude", crypto.longitude);
        i.putExtra("symbol", crypto.getSymbol());
        startActivity(i);
    }


    /**
     * Handle result from the trade dialog. Only gets called when the user confirms the trade.
     * @param amount fiat amount to trade
     */
    @Override
    public void getResult(double amount, String type) {
        // We get the fiat amount to purchase here, send POST request to server to complete trade
        app.createTrade(crypto.getSymbol(), "EUR", type, amount);
    }
}