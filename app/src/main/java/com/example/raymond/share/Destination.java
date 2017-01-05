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
import android.widget.EditText;
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

public class Destination extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar toolbar;
    private ImageButton next;
    private AutoCompleteTextView destination;
    private AutoComplete autoComplete = new AutoComplete();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Destination");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        destination = (AutoCompleteTextView) findViewById(R.id.destination);

        destination.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                autoComplete.setTextView(destination);
                autoComplete.setContext(getBaseContext());
                autoComplete.startToAutoComplete(s.toString());
                mMap.clear();
                placeMarker();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        next = (ImageButton) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText destination = (EditText) findViewById(R.id.destination);

                if (destination.getText().toString().trim().equals(""))
                    destination.setError("Destination is required!");
                else {

                    Intent intent = new Intent(getApplicationContext(), Source.class);
                    intent.putExtra("source", getIntent().getStringExtra("source"));
                    intent.putExtra("destination", destination.getText());
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng start = new LatLng(0, 0);
        mMap.addMarker(new MarkerOptions().position(start).title("Start here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
    }

    public void placeMarker() {

        String location = destination.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {

            Geocoder geocoder = new Geocoder(this);

            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
}
