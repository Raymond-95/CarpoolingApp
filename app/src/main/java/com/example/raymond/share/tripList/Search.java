package com.example.raymond.share.tripList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.raymond.share.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class Search extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText source;
    private EditText destination;
    private RadioGroup role;
    private RadioButton radio_role;
    private int PLACE_PICKER_REQUEST = 1;
    private static EditText currentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Search");

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        source = (EditText) findViewById(R.id.source);

        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentEditText = source;
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    startActivityForResult(builder.build(Search.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        destination = (EditText) findViewById(R.id.destination);

        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentEditText = destination;
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    startActivityForResult(builder.build(Search.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        TextView enter = (TextView) findViewById(R.id.enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                source.setError(null);
                destination.setError(null);
                role = (RadioGroup) findViewById(R.id.role);
                int selectedId = role.getCheckedRadioButtonId();
                radio_role = (RadioButton) findViewById(selectedId);
                String role;

                if (source.getText().toString().trim().equals("")) {
                    source.setError("Source is required!");
                }
                if (destination.getText().toString().trim().equals("")) {
                    destination.setError("Destination is required!");
                }
                if(radio_role.getText().equals("Driver")){
                    role = "driver";
                }
                else
                {
                    role = "passenger";
                }

                if (!source.getText().toString().trim().equals("") &&
                        !destination.getText().toString().trim().equals("")){
                    Intent intent = new Intent(getApplicationContext(), SearchList.class);
                    intent.putExtra("source", source.getText().toString());
                    intent.putExtra("destination", destination.getText().toString());
                    intent.putExtra("role", role);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                currentEditText.setText(place.getName());
            }
        }
    }
}
