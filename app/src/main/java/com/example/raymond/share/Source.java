package com.example.raymond.share;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.example.raymond.share.AutoComplete.AutoComplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Source extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar toolbar;
    private ImageButton next;
    private AutoCompleteTextView source;
    private AutoComplete autoComplete = new AutoComplete();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Source");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        source = (AutoCompleteTextView) findViewById(R.id.source);

        source.addTextChangedListener(new TextWatcher() {

            private final long DELAY = 2000; // milliseconds

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mMap.clear();
                placeMarker();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                autoComplete.setTextView(source);
                autoComplete.setContext(getBaseContext());
                autoComplete.startToAutoComplete(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(1);
                ScheduledFuture sf = scheduledPool.schedule(runnabledelayedTask, DELAY, TimeUnit.MILLISECONDS);
            }
        });

        next = (ImageButton) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (source.getText().toString().trim().equals(""))
                    source.setError("Source is required!");
                else {

                    Intent intent = new Intent(getApplicationContext(), Destination.class);
                    intent.putExtra("source", source.getText());
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng start = new LatLng(0, 0);
        mMap.addMarker(new MarkerOptions().position(start).title("Start here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.moveCamera(CameraUpdateFactory.zoomBy()
    }

    public void placeMarker() {

        String location = source.getText().toString();
        List<Address> addressList = null;
        if (!location.equals("")) {

            Geocoder geocoder = new Geocoder(this);

            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Runnable runnabledelayedTask = new Runnable(){
        @Override
        public void run(){
            mMap.clear();
            placeMarker();
        }
    };
}
