package com.vidkreca.cryptosimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment map;
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Configure MapView
        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        // Get coordinates from intent extras and create GeoPoint
        Bundle data = getIntent().getExtras();
        latitude  = data.getDouble("latitude");
        longitude = data.getDouble("longitude");

        // Set TextView's value
        ((TextView) findViewById(R.id.mapTV)).setText("Top holder of "+data.getString("symbol"));
    }


    @Override
    protected void onStart() {
        super.onStart();

        String str = String.format("Showing: %f, %f", latitude, longitude);
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng point = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions()
                .position(point)
                .title("Top holder"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));
    }
}