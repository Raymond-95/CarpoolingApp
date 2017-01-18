package com.example.raymond.share.tripList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.raymond.share.DownloadImage;
import com.example.raymond.share.EditTrip;
import com.example.raymond.share.R;
import com.example.raymond.share.UserProfile;
import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.Trip;
import com.example.raymond.share.model.User;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private static int id;
    private static Trip tripInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        String pass_id = getIntent().getStringExtra("id");
        int id = Integer.parseInt(pass_id);
        getTrip(id);
    }

    public void getTrip(int id) {

        ShareApi.init(getApplicationContext())
                .getTrip(id)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        tripInfo = new Trip(meta.getResult());

                        toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);

                        if (tripInfo.getRole().equals("driver"))
                            toolbar.setTitle("Driver");
                        else if (tripInfo.getRole().equals("passenger"))
                            toolbar.setTitle("Passenger");

                        CircleImageView profilePic = (CircleImageView) findViewById(R.id.profile_image);
                        profilePic.setBorderWidth(4);
                        profilePic.setBorderColor(getResources().getColor(R.color.white));
                        new DownloadImage(profilePic).execute(tripInfo.getImageUrl());

                        //direct user to user profile
                        next(tripInfo.getUserId());

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

                        //Set Button to th bottom
                        Button button = (Button) findViewById(R.id.send);
                        User user = new User().getUserAccount(getApplicationContext());
                        String from = getIntent().getStringExtra("from");
                        if (tripInfo.getUserId() == user.getId() && from.equals("fragment")){

                            button.setText("EDIT");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(getApplicationContext(), EditTrip.class);
                                    intent.putExtra("id", Integer.toString(tripInfo.getId()));
                                    startActivity(intent);
                                }
                            });
                        }
                        else if (tripInfo.getUserId() != user.getId() && from.equals("fragment")){

                            button.setText("SEND REQUEST");
                        }
                        else if (tripInfo.getUserId() == user.getId() && tripInfo.getStatus().equals("available") && from.equals("history")){

                            button.setText("EDIT");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(getApplicationContext(), EditTrip.class);
                                    intent.putExtra("id", Integer.toString(tripInfo.getId()));
                                    startActivity(intent);
                                }
                            });
                        }
                        else if (from.equals("history")){

                            RelativeLayout layout = (RelativeLayout) findViewById(R.id.tripDetail);
                            layout.removeView(button);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {

                    }

                });
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

    public void next(int temp_id){

        id = temp_id;

        ImageView profile = (ImageView) findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                intent.putExtra("id", Integer.toString(id));
                startActivity(intent);
            }
        });
    }
}
