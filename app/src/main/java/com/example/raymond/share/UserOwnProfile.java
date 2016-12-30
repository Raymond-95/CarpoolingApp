package com.example.raymond.share;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;

public class UserOwnProfile extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_own_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");

        TextView nameView = (TextView)findViewById(R.id.name);
        String username = getIntent().getStringExtra("username");
        nameView.setText(username);

        String profileId = Profile.getCurrentProfile().getId();
        Button emailBtn = (Button) findViewById(R.id.email);
        String email = getIntent().getStringExtra("email");
        emailBtn.setText(email);

        String imageUrl = getIntent().getStringExtra("imageUrl");
        new DownloadImage ((ImageView)findViewById(R.id.profileImage)).execute(imageUrl);

        Button fbProfileBtn = (Button) findViewById(R.id.fbProfile);
        fbProfileBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getApplicationContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
    }

    public String getFacebookPageURL(Context context) {

        Profile userId = Profile.getCurrentProfile();

        String FACEBOOK_PAGE_ID = userId.getId();
        String FACEBOOK_URL = "https://www.facebook.com/" + FACEBOOK_PAGE_ID;

        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
}
