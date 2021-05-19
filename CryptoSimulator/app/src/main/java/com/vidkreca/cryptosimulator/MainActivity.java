package com.vidkreca.cryptosimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private API api;
    private App app;

    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Set navigation bar event handlers
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
        api = app.getApi();

        // Create notification channel
        createNotificationChannel();

        // If this is the first startup, start the DifficultyActivity
        if (app.isFirstStart()) {
            Intent i = new Intent(this, DifficultyActivity.class);
            startActivity(i);
        }

        // Open HomeFragment as the default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new HomeFragment()).commit();
    }


    /**
     * Display a fragment in the MainActivity frame.
     * @param fragment fragment to display
     */
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "CryptoSimulator";
            String description = "Notification channel for CryptoSimulator";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("CryptoSimulator", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}