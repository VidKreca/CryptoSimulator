package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vidkreca.data.ProtocolMessages.List;
import com.vidkreca.data.ProtocolMessages.Single;

public class MainActivity extends AppCompatActivity {

    private API api;
    private App app;
    private CryptocurrencyAdapter adapter;

    private RecyclerView crypto_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = new API(getApplicationContext());
        app = (App)getApplication();

        // Assign UI elements
        crypto_rv = findViewById(R.id.crypto_recyclerview);

        GetData();
        InitAdapter();
    }


    private void InitAdapter() {
        adapter = new CryptocurrencyAdapter(app, new CryptocurrencyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

            }
            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });

        crypto_rv.setAdapter(adapter);
        crypto_rv.setLayoutManager(new LinearLayoutManager(this));
    }


    private void GetData() {
        api.GetList(new VolleyCallBack() {
            @Override
            public void onSuccess(String json) {
                List response = API.gson.fromJson(json, List.class);

                app.GetStore().UpdateCryptocurrencies(response.currencies);

                adapter.notifyDataSetChanged();

                Toast.makeText(getBaseContext(), app.GetStore().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getBaseContext(), "Could not retrieve list of cryptocurrencies.", Toast.LENGTH_LONG).show();
            }
        });
    }


    /*public void onButtonClick(View v) {
        api.GetList(new VolleyCallBack() {
            @Override
            public void onSuccess(String json) {
                List response = API.gson.fromJson(json, List.class);

                tv.setText(response.currencies[0].toString());
            }

            @Override
            public void onError(String message) {
                tv.setText(message);
            }
        });

        api.GetSingle(new VolleyCallBack() {
            @Override
            public void onSuccess(String json) {
                Single response = API.gson.fromJson(json, Single.class);

                tv.setText(response.GetCryptocurrency().toString());
            }

            @Override
            public void onError(String message) {
                tv.setText(message);
            }
        }, et.getText().toString());
    }*/
}