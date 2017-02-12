package com.example.raymond.share;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class RegTrip extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText source;
    private EditText destination;
    private EditText currentEditText;
    private static EditText trip_date;
    private EditText trip_time;
    private TextView role;
    private EditText information;
    private TextView create;
    private Calendar calendar = Calendar.getInstance();
    private String internationalTime;
    private String getRole;
    private static String date = "";
    private int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private int minute = calendar.get(Calendar.MINUTE);
    private static int PLACE_PICKER_REQUEST = 1;
    ProgressDialog mProgressDialog;
    private static final String TAG = "share.activity.reg_trip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_trip);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add Trip");

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

                    startActivityForResult(builder.build(RegTrip.this), PLACE_PICKER_REQUEST);
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

                    startActivityForResult(builder.build(RegTrip.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        trip_date = (EditText) findViewById(R.id.date);

        trip_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        trip_time = (EditText) findViewById(R.id.time);

        trip_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(RegTrip.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                internationalTime = hourOfDay + ":" + minute;
                                String AM_PM = "";
                                if(hourOfDay < 12) {
                                    if (hourOfDay == 0)
                                        hourOfDay = 12;

                                    AM_PM = "AM";
                                } else {
                                    if (hourOfDay != 12)
                                        hourOfDay = hourOfDay - 12;

                                    AM_PM = "PM";
                                }

                                String newMinute = String.valueOf(minute);
                                if (minute < 10){
                                    newMinute = "0" + minute;
                                }

                                trip_time.setText(hourOfDay + ":" + newMinute + " " + AM_PM );
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        getRole = getIntent().getStringExtra("role");

        role = (TextView) findViewById(R.id.role);
        role.setText(getRole);


        create = (TextView) findViewById(R.id.create);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                source.setError(null);
                destination.setError(null);
                trip_date.setError(null);
                trip_time.setError(null);

                if (source.getText().toString().trim().equals("")) {
                    source.setError("Source is required!");
                }
                if (destination.getText().toString().trim().equals("")) {
                    destination.setError("Destination is required!");
                }
                if (trip_date.getText().toString().trim().equals("")) {
                    trip_date.setError("Date is required!");
                }
                if (trip_time.getText().toString().trim().equals("")) {
                    trip_time.setError("Time is required!");
                }

                if(!source.getText().toString().trim().equals("") &&
                        !destination.getText().toString().trim().equals("") &&
                        !trip_date.getText().toString().trim().equals("")  &&
                        !trip_time.getText().toString().trim().equals("")){

                    information = (EditText) findViewById(R.id.information);

                    registerTrip(source.getText().toString(),
                            destination.getText().toString(),
                            date,
                            internationalTime,
                            getRole,
                            information.getText().toString());

                    Intent intent = new Intent(getApplicationContext(), Homepage.class);
                    startActivity(intent);

                    finish();
                }
            }
        });
    }

    private void registerTrip(String source, String destination, String date, String time, String role, String information){

        mProgressDialog = ShareApi.init(this)
                .registerTrip(
                        source,
                        destination,
                        date,
                        time,
                        role,
                        information

                ).call(new ShareApi.DialogResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {
                        try {
                            Log.e(TAG, "Register trip is successfully done.");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {
                    }

                })
                .keepProgressDialog()
                .getProgressDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                currentEditText.setText(place.getName());
                Log.i(TAG, "Place: " + place.getName());
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            trip_date.setText( day + "-" + month + 1 + "-" + year);

            if (month+1 == 0)
                month = 12;
            else
                month = month+1;

            date = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is called, below method will be called.
        // The arguments will be working to get the Day of Week to show it in a special TextView for it.

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            trip_time.setText(month1 + "/" + day1 + "/" + year1);
            trip_time.setText(DateFormat.format("EEEE", new Date(selectedYear, selectedMonth, selectedDay - 1)).toString());
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mProgressDialog = null;
        trip_date = null;
        date = null;
    }
}
