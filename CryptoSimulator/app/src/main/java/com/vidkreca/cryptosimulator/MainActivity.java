package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vidkreca.data.ProtocolMessages.List;

public class MainActivity extends AppCompatActivity {

    private API api;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = new API(getApplicationContext());

        tv = findViewById(R.id.textView);
    }


    public void onButtonClick(View v) {
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
    }
}