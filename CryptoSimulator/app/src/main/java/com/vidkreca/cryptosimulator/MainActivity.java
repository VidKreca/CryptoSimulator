package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private API api;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = new API(getApplicationContext());

        tv = findViewById(R.id.textView);
        tv.setText("loaded");
    }


    public void onButtonClick(View v) {
        tv.setText("clicked");

        api.GetList(new VolleyCallBack() {
            @Override
            public void onSuccess(String json) {
                tv.setText(json);
            }

            @Override
            public void onError(String message) {
                tv.setText(message);
            }
        });
    }
}