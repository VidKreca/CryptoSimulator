package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.squareup.picasso.Picasso;
import com.vidkreca.data.Cryptocurrency;
import com.vidkreca.data.PortfolioItem;
import com.vidkreca.data.ProtocolMessages.Single;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SingleActivity extends AppCompatActivity implements TradeDialog.TradeDialogListener {

    private API api;
    private App app;
    private Cryptocurrency crypto;
    private String symbol_query;

    private TextView name;
    private TextView price;

    private LineChart chart;


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


        // Chart config
        chart = findViewById(R.id.chart);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);
        chart.setNoDataText("No price data for this cryptocurrency.");
        chart.getDescription().setText("7-day price history");
        chart.getDescription().setTextColor(Color.BLUE);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setMaxVisibleValueCount(5);
        chart.animateX(1500);

        // Disable chart labels and lines
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawAxisLine(false);

        // Configure chart marker
        IMarker marker = new ChartMarker(getApplicationContext(), R.layout.chart_marker);
        chart.setMarker(marker);


        getData();
    }


    /**
     * Get all data about this cryptocurrency.
     */
    private void getData() {
        api.getSingle(new VolleyCallback() {
            @Override
            public void onSuccess(String json) {
                Single response = API.gson.fromJson(json, Single.class);
                crypto = response.GetCryptocurrency();

                // Assign retrieved values to UI elements
                name.setText(crypto.getName());
                price.setText(Double.toString(crypto.getPrice()) + "€");    // TODO - consider currently selected FIAT currency

                // Assign priceData to chart
                setPriceData(response.priceData);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getBaseContext(), "Could not retrieve cryptocurrency statistics.\n"+message, Toast.LENGTH_LONG).show();
            }
        }, symbol_query);
    }


    /**
     * Update chart with price data for this cryptocurrency
     * @param priceData an array of high prices for a time period
     */
    private void setPriceData(double[] priceData) {
        ArrayList<Entry> values = new ArrayList<>();

        // Fill values ArrayList with values from API
        for (int i = 0; i < priceData.length; i++) {
            values.add(new Entry(i, (float)priceData[i]));
        }

        LineDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
        } else {
            set1 = new LineDataSet(values, "");



            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setDrawCircles(false);

            // line thickness and point size
            set1.setLineWidth(1f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            set1.setFillColor(Color.GRAY);







            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            chart.setData(data);
        }

        chart.invalidate();
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
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

    /**
     * Open a trade dialog if the user has enough balance to make a trade.
     */
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
     * Handle result from the trade dialog. Only gets called when the user confirms the trade.
     * @param amount fiat amount to trade
     */
    @Override
    public void getResult(double amount, String type) {
        // We get the fiat amount to purchase here, send POST request to server to complete trade
        app.createTrade(crypto.getSymbol(), "EUR", type, amount);
    }
}