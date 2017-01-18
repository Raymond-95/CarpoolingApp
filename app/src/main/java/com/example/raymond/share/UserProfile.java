package com.example.raymond.share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.User;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");

        String pass_id = getIntent().getStringExtra("id");
        int id = Integer.parseInt(pass_id);
        getUser(id);
    }

    public void getUser(int id) {

        ShareApi.init(getApplicationContext())
                .getUser(id)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        user = new User(meta.getResult());

                        TextView nameView = (TextView)findViewById(R.id.name);
                        nameView.setText(user.getName());

                        Button emailBtn = (Button) findViewById(R.id.email);
                        emailBtn.setText(user.getEmail());

                        Button phoneBtn = (Button) findViewById(R.id.phone);
                        phoneBtn.setText(user.getPhonenum());

                        CircleImageView profilePic = (CircleImageView)findViewById(R.id.profileImage);
                        profilePic.setBorderWidth(4);
                        profilePic.setBorderColor(getResources().getColor(R.color.white));
                        new DownloadImage (profilePic).execute(user.getImageUrl());

                        Button fbProfileBtn = (Button) findViewById(R.id.fbProfile);
                        fbProfileBtn.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                                String facebookUrl = user.getProfileUrl();
                                facebookIntent.setData(Uri.parse(facebookUrl));
                                startActivity(facebookIntent);
                            }
                        });

                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {

                    }

                });
    }

}
