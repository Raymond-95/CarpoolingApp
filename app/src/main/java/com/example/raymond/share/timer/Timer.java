package com.example.raymond.share.timer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.raymond.share.Homepage;
import com.example.raymond.share.R;
import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;

import org.json.JSONObject;

public class Timer extends Activity {

    /**
     * Key for getting saved start time of Chronometer class
     * this is used for onResume/onPause/etc.
     */
    public static final String START_TIME = "START_TIME";
    /**
     * Same story, but to tell whether the Chronometer was running or not
     */
    public static final String CHRONO_WAS_RUNNING = "CHRONO_WAS_RUNNING";
    /**
     * Same story, but if chronometer was stopped, we dont want to lose the stop time shows in
     * the tv_timer
     */
    public static final String TV_TIMER_TEXT = "TV_TIMER_TEXT";
    /**
     * Same story...keeps the value of the lap counter
     */
    public static final String LAP_COUNTER  = "LAP_COUNTER";

    //Member variables to access UI Elements
    Button mBtn_start, mBtn_end; //buttons
    TextView mTvTimer; //timer textview
    private static  AlertDialog dialog;

    //keep track of how many times btn_lap was clicked
    int mLapCounter = 1;

    //Instance of Chronometer
    Chronometer mChrono;

    //Thread for chronometer
    Thread mThreadChrono;

    Context mContext;

    private static int recipient;
    private int count;
    private double start_lat;
    private double start_lng;
    private AlarmManager manager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        recipient = getIntent().getIntExtra("recipient", 0);

        //Instantiating all member variables

        mContext = this;

        mBtn_start = (Button) findViewById(R.id.btn_start);
        mBtn_end = (Button) findViewById(R.id.btn_finish);

        mTvTimer = (TextView) findViewById(R.id.tv_timer);

        //btn_start click handler
        mBtn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the chronometer has not been instantiated before...
                count = 0;
                if (mChrono == null) {
                    //instantiate the chronometer
                    mChrono = new Chronometer(mContext);
                    //run the chronometer on a separate thread
                    mThreadChrono = new Thread(mChrono);
                    mThreadChrono.start();

                    //start the chronometer!
                    mChrono.start();

                    //reset the lap counter
                    mLapCounter = 1;
                }

                GPSTracker gps;

                gps = new GPSTracker(getApplicationContext());

                // check if GPS enabled
                if(gps.canGetLocation()){

                    start_lat = gps.getLatitude();
                    start_lng = gps.getLongitude();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

                startAlarm(recipient);

                mBtn_start.setVisibility(View.GONE);
                mBtn_end.setVisibility(View.VISIBLE);
            }
        });


        //btn_end click handler
        mBtn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the chronometer has not been instantiated before...

                    //stop the chronometer
                    mChrono.stop();
                    //stop the thread
                    mThreadChrono.interrupt();
                    mThreadChrono = null;
                    //kill the chrono class
                    mChrono = null;

                    manager.cancel(pendingIntent);

                    popUpWindow();
            }
        });
    }

    private String sentence;

    public void popUpWindow() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Timer.this);

        String text = mTvTimer.getText().toString();

        String duration;
        if (Integer.parseInt(text.split(":")[0]) != 0){

            if (Integer.parseInt(text.split(":")[1]) == 1){
                duration = Integer.parseInt(text.split(":")[0]) + "hour " + Integer.parseInt(text.split(":")[1]) + "minute " + Integer.parseInt(text.split(":")[2]) + "seconds";
            }
            else{
                duration = Integer.parseInt(text.split(":")[0]) + "hour " + Integer.parseInt(text.split(":")[1]) + "minutes " + Integer.parseInt(text.split(":")[2]) + "seconds";
            }
        }
        else if (Integer.parseInt(text.split(":")[1]) != 0){
            if (Integer.parseInt(text.split(":")[1]) == 1){
                duration = Integer.parseInt(text.split(":")[1]) + "minute " + Integer.parseInt(text.split(":")[2]) + "seconds";
            }
            else{
                duration = Integer.parseInt(text.split(":")[1]) + "minutes " + Integer.parseInt(text.split(":")[2]) + "seconds";
            }
        }
        else{
            duration = Integer.parseInt(text.split(":")[2]) + "seconds";
        }

        sentence = "Congratulation, you have reached your destination after " + duration;
        mBuilder.setMessage(sentence);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                ratingPop();
                dialog.dismiss();
                mTvTimer.setText("00:00:00");
            }
        });

        dialog = mBuilder.create();
        dialog.show();
    }

    public void ratingPop() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Timer.this);
        View root = ((LayoutInflater) Timer.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.rating, null);

        final RatingBar rating = (RatingBar) root.findViewById(R.id.ratingbar);

        Drawable drawable = rating.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFDA44"), PorterDuff.Mode.SRC_ATOP);
        mBuilder.setMessage("Having a good trip with your partner? Please give him a rate.");

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                rating(rating.getNumStars(), recipient, getIntent().getIntExtra("trip_id", 0));
                dialog.dismiss();
            }
        });

        mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                rating(0, recipient, getIntent().getIntExtra("trip_id", 0));
                dialog.dismiss();
            }
        });

        mBuilder.setView(root);
        dialog = mBuilder.create();
        dialog.show();
    }

    /**
     * Update the text of tv_timer
     * @param timeAsText the text to update tv_timer with
     */
    public void updateTimerText(final String timeAsText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvTimer.setText(timeAsText);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadInstance();

        //stop background services and notifications
        ((ChronometerApplication)getApplication()).stopBackgroundServices();
        ((ChronometerApplication)getApplication()).cancelNotification();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveInstance();

        if(mChrono != null && mChrono.isRunning()) {
            //start background notification and timer
            ((ChronometerApplication)getApplication())
                    .startBackgroundServices(mChrono.getStartTime());
        }
    }

    @Override
    protected void onDestroy() {

        saveInstance();

        //When back button is pressed, app will be destoyed by OS. We do not want this to stop us
        //from showing the notification if the chronometer is running!
        if(mChrono == null || !mChrono.isRunning()) {
            //stop background services and notifications
            ((ChronometerApplication) getApplication()).stopBackgroundServices();
            ((ChronometerApplication) getApplication()).cancelNotification();
        }

        super.onDestroy();
    }

    /**
     * If the application goes to background or orientation change or any other possibility that
     * will pause the application, we save some instance values, to resume back from last state
     */
    private void saveInstance() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //TODO move tags to a static class
        if(mChrono != null && mChrono.isRunning()) {
            editor.putBoolean(CHRONO_WAS_RUNNING, mChrono.isRunning());
            editor.putLong(START_TIME, mChrono.getStartTime());
            editor.putInt(LAP_COUNTER, mLapCounter);
        } else {
            editor.putBoolean(CHRONO_WAS_RUNNING, false);
            editor.putLong(START_TIME, 0); //0 means chronometer was not active! a redundant check!
            editor.putInt(LAP_COUNTER, 1);
        }

        //Same story for timer text
        editor.putString(TV_TIMER_TEXT, mTvTimer.getText().toString());

        editor.commit();
    }

    /**
     * Load the shared preferences to resume last known state of the application
     */
    private void loadInstance() {

        SharedPreferences pref = getPreferences(MODE_PRIVATE);

        //if chronometer was running
        if(pref.getBoolean(CHRONO_WAS_RUNNING, false)) {
            //get the last start time from the bundle
            long lastStartTime = pref.getLong(START_TIME, 0);
            //if the last start time is not 0
            if(lastStartTime != 0) { //because 0 means value was not saved correctly!

                if(mChrono == null) { //make sure we dont create new instance and thread!

                    if(mThreadChrono != null) { //if thread exists...first interrupt and nullify it!
                        mThreadChrono.interrupt();
                        mThreadChrono = null;
                    }

                    //start chronometer with old saved time
                    mChrono = new Chronometer(mContext, lastStartTime);
                    mThreadChrono = new Thread(mChrono);
                    mThreadChrono.start();
                    mChrono.start();
                }
            }
        }

        //we will load the lap text anyway in any case!
        //set the old value of lap counter
        mLapCounter = pref.getInt(LAP_COUNTER, 1);

        String oldTvTimerText = pref.getString(TV_TIMER_TEXT, "");
        if(!oldTvTimerText.isEmpty()){
            mTvTimer.setText(oldTvTimerText);
        }
    }

    private void startAlarm(int recipient) {

        Intent myIntent;

        myIntent = new Intent(Timer.this, AlarmGPS.class);
        myIntent.putExtra("recipient", recipient);
        myIntent.putExtra("count", Integer.toString(count));
        if (count == 0){
            myIntent.putExtra("start_lat", start_lat);
            myIntent.putExtra("start_lng", start_lng);
        }

        pendingIntent = PendingIntent.getBroadcast(this,0,myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000,5000,pendingIntent);

        count = 1;
    }

    public void rating(int rate, int rate_to, int trip_id) {

        ShareApi.init(this)
                .rating(
                        rate,
                        rate_to,
                        trip_id
                ).call(
                        new ShareApi.DialogResponseHandler(){

                            @Override
                            public void onSuccess(JSONObject response, ShareJSON meta){

                                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                                startActivity(intent);

                                finish();
                            }

                            @Override
                            public void onFailure(Throwable e, JSONObject response, ShareJSON meta){
                                Log.e("Error", "Rating failed!");
                            }
                        }
                );
    }
}
