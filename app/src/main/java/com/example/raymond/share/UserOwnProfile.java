package com.example.raymond.share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raymond.share.model.User;

public class UserOwnProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_own_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");

        user = new User().getUserAccount(getApplicationContext());

        TextView nameView = (TextView)findViewById(R.id.name);
        nameView.setText(user.getName());

        Button emailBtn = (Button) findViewById(R.id.email);
        emailBtn.setText(user.getEmail());

        new DownloadImage ((ImageView)findViewById(R.id.profileImage)).execute(user.getImageUrl());

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


}
