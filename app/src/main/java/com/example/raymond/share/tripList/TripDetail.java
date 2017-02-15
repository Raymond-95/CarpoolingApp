package com.example.raymond.share.tripList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raymond.share.DownloadImage;
import com.example.raymond.share.EditTrip;
import com.example.raymond.share.R;
import com.example.raymond.share.UserProfile;
import com.example.raymond.share.chat.Message;
import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.Trip;
import com.example.raymond.share.model.User;
import com.example.raymond.share.timer.Timer;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private Trip tripInfo;
    private Button send;
    private int temp_id;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        String pass_id = getIntent().getStringExtra("id");
        int id = Integer.parseInt(pass_id);
        getTrip(id);
    }

    public void getTrip(int id) {

        mProgressDialog =ShareApi.init(this)
                .setProgressDialog(mProgressDialog)
                .getTrip(id)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        tripInfo = new Trip(meta.getResult());

                        toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);

                        toolbar.setNavigationIcon(R.drawable.back);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                onBackPressed();
                            }
                        });

                        if (tripInfo.getRole().equals("driver"))
                            toolbar.setTitle("Driver");
                        else if (tripInfo.getRole().equals("passenger"))
                            toolbar.setTitle("Passenger");

                        CircleImageView profilePic = (CircleImageView) findViewById(R.id.profile_image);
                        profilePic.setBorderWidth(4);
                        profilePic.setBorderColor(getResources().getColor(R.color.white));
                        new DownloadImage(profilePic).execute(tripInfo.getImageUrl());

                        ImageView background = (ImageView) findViewById(R.id.background);
                        new DownloadImage(background).execute(tripInfo.getImageUrl());
                        background.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);

                        //direct user to user profile
                        next(tripInfo.getUserId());
                        chat(tripInfo.getUserId());

                        TextView name = (TextView) findViewById(R.id.name);
                        name.setText(tripInfo.getName());

                        TextView source = (TextView) findViewById(R.id.item_source);
                        source.setText(tripInfo.getSource());

                        TextView destination = (TextView) findViewById(R.id.item_destination);
                        destination.setText(tripInfo.getDestination());

                        TextView date = (TextView) findViewById(R.id.item_date);
                        date.setText(tripInfo.getDate());

                        // format time for display
                        String formattedTime = time(tripInfo.getTime());
                        TextView time = (TextView) findViewById(R.id.item_time);
                        time.setText(formattedTime);

                        TextView information = (TextView) findViewById(R.id.item_info);
                        information.setText(tripInfo.getInformation());

                        //Set Button to the bottom
                        send = (Button) findViewById(R.id.send);
                        User user = new User().getUserAccount(getApplicationContext());
                        String from = getIntent().getStringExtra("from");
                        if (tripInfo.getUserId() == user.getId() && from.equals("fragment") && tripInfo.getStatus().equals("available")){

                            send.setText("EDIT");
                            send.setVisibility(View.VISIBLE);
                            send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(getApplicationContext(), EditTrip.class);
                                    intent.putExtra("id", Integer.toString(tripInfo.getId()));
                                    startActivity(intent);
                                }
                            });
                        }
                        else if (tripInfo.getUserId() != user.getId() && from.equals("fragment") && tripInfo.getStatus().equals("available") ||tripInfo.getStatus().equals("cancelled") ){

                            send.setText("SEND REQUEST");
                            send.setVisibility(View.VISIBLE);
                            send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    sendRequest(tripInfo.getId(), "triprequest");
                                    send.setVisibility(View.GONE);

                                }
                            });
                        }
                        else if (tripInfo.getUserId() != user.getId() && from.equals("history") && tripInfo.getStatus().equals("approved")){

                            send.setText("START TRIP");
                            send.setVisibility(View.VISIBLE);
                            send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(getApplicationContext(), Timer.class);
                                    intent.putExtra("recipient", tripInfo.getUserId());
                                    intent.putExtra("trip_id", tripInfo.getId());
                                    startActivity(intent);

                                }
                            });
                        }
                        else if (tripInfo.getUserId() == user.getId() && tripInfo.getStatus().equals("available") && from.equals("history")){

                            send.setText("EDIT");
                            send.setVisibility(View.VISIBLE);
                            send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(getApplicationContext(), EditTrip.class);
                                    intent.putExtra("id", Integer.toString(tripInfo.getId()));
                                    startActivity(intent);
                                }
                            });
                        }

                        final ImageButton addGuardian = (ImageButton) findViewById(R.id.addGuardian);

                        if (tripInfo.getStatus().equals("approved")){
                            addGuardian.setVisibility(View.VISIBLE);
                        }

                        addGuardian.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendRequest(tripInfo.getId(), "guardian");
                                addGuardian.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {

                    }

                })
                .keepProgressDialog()
                .getProgressDialog();
    }

    public String time(String temp){

        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("hh:mm aa");
        String temp_time = null;
        try {
            Date temp_date = format1.parse(temp);
            temp_time = format2.format(temp_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return temp_time;
    }

    public void next(int id){

        this.temp_id = id;

        ImageButton profile = (ImageButton) findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                intent.putExtra("id", Integer.toString(temp_id));
                startActivity(intent);
            }
        });
    }

    public void chat(int recipient){

        this.temp_id = recipient;

        ImageButton chat = (ImageButton) findViewById(R.id.chat);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Message.class);
                intent.putExtra("id", Integer.toString(temp_id));
                startActivity(intent);
            }
        });
    }

    public void sendRequest(int id, String type) {

        mProgressDialog =ShareApi.init(this)
                .setProgressDialog(mProgressDialog)
                .sendRequest(id, type)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        Log.e("Share", "Request is sending out.");
                    }
                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {
                        Log.e("Share", e.toString());
                    }

                })
                .keepProgressDialog()
                .getProgressDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
