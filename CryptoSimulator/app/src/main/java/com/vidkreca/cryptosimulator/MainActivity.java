package com.vidkreca.cryptosimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vidkreca.data.ProtocolMessages.List;
import com.vidkreca.data.User;

public class MainActivity extends AppCompatActivity {

    private API api;
    private App app;

    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                openFragment(HomeFragment.newInstance());
                                return true;
                            case R.id.navigation_portfolio:
                                openFragment(PortfolioFragment.newInstance());
                                return true;
                            case R.id.navigation_settings:
                                openFragment(SettingsFragment.newInstance());
                                return true;
                        }
                        return false;
                    }
                };
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        app = (App)getApplication();
        api = app.GetApi();

        // Debugging, remove UUID from SharedPreferences
        app.ResetUUID();

        // If this is the first startup, start the DifficultyActivity
        if (app.IsFirstStart()) {
            Intent i = new Intent(this, DifficultyActivity.class);
            startActivity(i);
        }

        // Set HomeFragment as the default
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new HomeFragment()).commit();
    }


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}