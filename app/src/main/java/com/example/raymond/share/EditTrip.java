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
import com.example.raymond.share.model.Trip;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class EditTrip extends AppCompatActivity {

    private Toolbar toolbar;
    private static Trip tripInfo;
    private static EditText source;
    private static EditText destination;
    private static EditText currentEditText;
    private static EditText trip_date;
    private static EditText trip_time;
    private static TextView role;
    private static EditText information;
    private static Calendar calendar = Calendar.getInstance();
    private static String internationalTime;
    private static String getRole;
    private static String date = "";
    private int id;
    private int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private int minute = calendar.get(Calendar.MINUTE);
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    ProgressDialog mProgressDialog;
    private static final String TAG = "share.activity.editTrip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Trip");

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        source = (EditText) findViewById(R.id.source);
        destination = (EditText) findViewById(R.id.destination);
        trip_date = (EditText) findViewById(R.id.date);
        trip_time = (EditText) findViewById(R.id.time);
        role = (TextView) findViewById(R.id.role);
        information = (EditText) findViewById(R.id.information);

        String pass_id = getIntent().getStringExtra("id");
        id = Integer.parseInt(pass_id);
        getTrip(id);

        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentEditText = source;

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(EditTrip.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentEditText = destination;

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(EditTrip.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        trip_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        trip_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditTrip.this,
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

        TextView edit = (TextView) findViewById(R.id.edit);

        edit.setOnClickListener(new View.OnClickListener() {
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

                    updateTrip(id,
                            source.getText().toString(),
                            destination.getText().toString(),
                            date,
                            internationalTime,
                            role.getText().toString(),
                            information.getText().toString());

                    Intent intent = new Intent(getApplicationContext(), Homepage.class);
                    startActivity(intent);

                    finish();
                }
            }
        });
    }

    public void getTrip(int id) {

        ShareApi.init(getApplicationContext())
                .getTrip(id)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        tripInfo = new Trip(meta.getResult());

                        source.setText(tripInfo.getSource());
                        destination.setText(tripInfo.getDestination());
                        trip_date.setText(tripInfo.getDate());
                        trip_time.setText(tripInfo.getTime());
                        role.setText((tripInfo.getRole()));
                        information.setText(tripInfo.getInformation());
                    }
                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {

                    }

                });
    }

    private void updateTrip(int id, String source, String destination, String date, String time, String role, String information){

        mProgressDialog = ShareApi.init(this)
                .setProgressDialog(mProgressDialog)
                .updateTrip(
                        id,
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
                            Log.e(TAG, "Trip is successfully update.");

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
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                currentEditText.setText(place.getName());
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
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
        tripInfo = null;
        source = null;
        destination = null;
        currentEditText = null;
        trip_date = null;
        trip_time = null;
        role = null;
        information = null;
        calendar = null;
        internationalTime = null;
        getRole = null;
        date = null;
    }
}
