package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vidkreca.data.ProtocolMessages.List;
import com.vidkreca.data.User;

public class MainActivity extends AppCompatActivity {

    private API api;
    private App app;
    private CryptocurrencyAdapter adapter;

    private RecyclerView crypto_rv;
    private TextView balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (App)getApplication();
        api = app.GetApi();

        // Debugging, remove UUID from SharedPreferences
        //app.ResetUUID();

        // If this is the first startup, start the DifficultyActivity
        if (app.IsFirstStart()) {
            Intent i = new Intent(this, DifficultyActivity.class);
            startActivity(i);
        } else {
            // Assign UI elements
            crypto_rv = findViewById(R.id.crypto_recyclerview);
            balance = findViewById(R.id.balance);

            GetData(null);
            GetUser();
            InitAdapter();
            InitSwipeToRefresh();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // This might be really slowing down the app, remove if necessary
        if (app.IsFirstStart()) {
            Intent i = new Intent(this, DifficultyActivity.class);
            startActivity(i);
        }
    }


    private void InitAdapter() {
        adapter = new CryptocurrencyAdapter(app, new CryptocurrencyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                String symbol_query = app.GetStore().GetAtIndex(position).getSymbol();

                Intent i = new Intent(getBaseContext(), SingleActivity.class);
                i.putExtra("symbol", symbol_query);

                startActivity(i);
            }
            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });

        crypto_rv.setAdapter(adapter);
        crypto_rv.setLayoutManager(new LinearLayoutManager(this));
    }


    private void GetData(SwipeRefreshLayout pullToRefresh) {
        api.GetList(new VolleyCallback() {
            @Override
            public void onSuccess(String json) {
                List response = API.gson.fromJson(json, List.class);

                app.GetStore().UpdateCryptocurrencies(response.currencies);

                adapter.notifyDataSetChanged();

                if (pullToRefresh != null)
                    pullToRefresh.setRefreshing(false);     // Signal that we have finished refreshing

                //Toast.makeText(getBaseContext(), app.GetStore().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getBaseContext(), "Could not retrieve list of cryptocurrencies.\n"+message, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void GetUser() {
        String uuid = app.GetUUID();

        // Do not perform request if uuid is null
        if (uuid == null)
            return;

        api.GetUser(new VolleyCallback() {
            @Override
            public void onSuccess(String json) {
                User response = API.gson.fromJson(json, User.class);
                app.SetUser(response);

                // Update UI
                String balanceStr = Double.toString(app.GetUser().getBalance()) + "€";  // TODO - add different fiat symbols
                balance.setText(balanceStr);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getBaseContext(), "Could not retrieve user.\n"+message, Toast.LENGTH_LONG).show();
            }
        }, uuid);
    }


    private void InitSwipeToRefresh() {
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData(pullToRefresh);
                GetUser();
            }
        });
    }
}