package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vidkreca.data.ProtocolMessages.Difficulty;
import com.vidkreca.data.ProtocolMessages.List;
import com.vidkreca.data.ProtocolMessages.Options;

import java.util.ArrayList;
import java.util.Arrays;

public class DifficultyActivity extends AppCompatActivity {

    private App app;
    private API api;

    private ArrayList<Difficulty> difficulties;
    private DifficultyAdapter adapter;

    private RecyclerView difficulty_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        app = (App)getApplication();
        api = new API(getApplicationContext());

        // Assign UI elements
        difficulty_rv = findViewById(R.id.difficulty_recyclerview);

        GetData();
    }


    private void GetData() {
        api.GetList(new VolleyCallBack() {
            @Override
            public void onSuccess(String json) {
                Options response = API.gson.fromJson(json, Options.class);

                difficulties = new ArrayList<>(Arrays.asList(response.startingDifficulties));
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getBaseContext(), "Could not retrieve options.\n"+message, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void InitAdapter() {
        adapter = new DifficultyAdapter(app, (Difficulty[])difficulties.toArray(), new DifficultyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Difficulty chosenDifficulty = difficulties.get(position);
            }
        });

        difficulty_rv.setAdapter(adapter);
        difficulty_rv.setLayoutManager(new LinearLayoutManager(this));
    }
}