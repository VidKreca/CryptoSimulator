package com.vidkreca.cryptosimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MapActivity extends AppCompatActivity {

    private MapView map;
    private GeoPoint point;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Configure MapView
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        // Get coordinates from intent extras and create GeoPoint
        Bundle data = getIntent().getExtras();
        double latitude  = data.getDouble("latitude");
        double longitude = data.getDouble("longitude");
        point = new GeoPoint(latitude, longitude);

        // Set TextView's value
        ((TextView) findViewById(R.id.mapTV)).setText("Top holder of "+data.getString("symbol"));
    }


    @Override
    protected void onStart() {
        super.onStart();

        centerOnPoint(point);
    }

    private void centerOnPoint(GeoPoint point) {
        IMapController mapController = map.getController();
        mapController.animateTo(point);
        mapController.setCenter(point);
    }
}