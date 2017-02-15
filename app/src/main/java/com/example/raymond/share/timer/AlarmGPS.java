package com.example.raymond.share.timer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;

import org.json.JSONObject;

/**
 * Created by Raymond on 12/2/2017.
 */
public class AlarmGPS extends BroadcastReceiver {

    GPSTracker gps;

    private PendingIntent mPingAlarmPendIntent;
    private static final String PING_ALARM = "com.sithagi.PING_ALARM";
    private Intent mPingAlarmIntent = new Intent(PING_ALARM);
    private BroadcastReceiver mPingAlarmReceiver;
    private double current_lat;
    private double curent_lng;
    private static double start_lat;
    private static double start_lng;

    @Override
    public void onReceive(Context context, Intent intent) {

        gps = new GPSTracker(context);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double current_lat = gps.getLatitude();
            double current_lng = gps.getLongitude();

            int recipient = intent.getIntExtra("recipient", 0);

            String number = intent.getStringExtra("count");
            int count = Integer.parseInt(number);

            if (count == 0){
                start_lat = intent.getDoubleExtra("start_lat", 0.00);
                start_lng = intent.getDoubleExtra("start_lng", 0.00);
            }

            start_lat = current_lat;
            start_lng = current_lng;

            alertGuardian(context, recipient, start_lat, start_lng, current_lat, current_lng);

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    public void alertGuardian(Context context, int recipient, double start_lat, double start_lng, double current_lat, double current_lng ) {

          ShareApi.init(context)
                .alertGuardian(recipient, start_lat, start_lng, current_lat, current_lng)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        Log.e("Share", "Action is taken");
                    }
                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {
                        Log.e("Share", e.toString());
                    }

                })
                .keepProgressDialog()
                .getProgressDialog();
    }
}
