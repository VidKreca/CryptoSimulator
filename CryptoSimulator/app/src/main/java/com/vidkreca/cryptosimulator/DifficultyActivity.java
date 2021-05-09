package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vidkreca.data.ProtocolMessages.Difficulty;
import com.vidkreca.data.ProtocolMessages.Options;
import com.vidkreca.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;


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
        api = app.getApi();

        difficulties = new ArrayList<>();

        // Assign UI elements
        difficulty_rv = findViewById(R.id.difficulty_recyclerview);

        initAdapter();
        getData();
    }


    private void initAdapter() {
        adapter = new DifficultyAdapter(app, difficulties, new DifficultyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Difficulty chosenDifficulty = difficulties.get(position);

                Toast.makeText(getBaseContext(), "Chosen difficulty: "+chosenDifficulty.difficulty, Toast.LENGTH_LONG).show();

                try {
                    chooseDifficulty(chosenDifficulty);
                } catch (JSONException ex) {
                    Log.e("CryptoSimulator", ex.getMessage());
                }
            }
        });

        difficulty_rv.setAdapter(adapter);
        difficulty_rv.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Get difficulties from the server and add them to the ArrayList
     */
    private void getData() {
        api.getOptions(new VolleyCallback() {
            @Override
            public void onSuccess(String json) {
                Options response = API.gson.fromJson(json, Options.class);

                difficulties.clear();
                difficulties.addAll(Arrays.asList(response.startingDifficulties));

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getBaseContext(), "Could not retrieve options.\n"+message, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void chooseDifficulty(Difficulty d) throws JSONException {
        // Generate UUID & save it to SharedPreferences
        String uuid = UUID.randomUUID().toString();
        app.setUUID(uuid);

        // Send account creation POST request and get our user object
        JSONObject data = new JSONObject();
        data.put("uuid", uuid);
        data.put("starting_balance", d.balance);
        api.createAccount(new VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject json) throws JSONException {
                // We get a User json response here
                User u = new User(json.getString("_id"), json.getString("uuid"), json.getDouble("balance"));
                app.setUser(u);

                // After successfully setting our User object, return to MainActivity
                finish();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getBaseContext(), "Could not make an account.\n"+message, Toast.LENGTH_LONG).show();
            }
        }, data);
    }
}