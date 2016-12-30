package com.example.raymond.share;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;

public class Homepage extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private static String username = "name";
    private static String email = "email";
    private static String imageUrl = "imageUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(Color.parseColor("#808080"), Color.parseColor("#3F51B5"));

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new DriverFragment(), "Driver");
        viewPagerAdapter.addFragments(new PassengerFragment(), "Passenger");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        getHeaderInfo();

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            // This method will trigger on item Click of navigation menu

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                menuItem.setChecked(false);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.mycarpools:
                        Toast.makeText(getApplicationContext(), "My carpools Selected", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), Mycarpools.class));
                        return true;
                    case R.id.friends:
                        Toast.makeText(getApplicationContext(), "Friends Selected", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), Friends.class));
                        return true;
                    case R.id.messages:
                        Toast.makeText(getApplicationContext(), "Messages Selected", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), Messages.class));
                        return true;
                    case R.id.logout:
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.homepage);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    public void getHeaderInfo(){

        View menuHeader =  navigationView.getHeaderView(0);

        Profile profile = Profile.getCurrentProfile();

        TextView nameView = (TextView) menuHeader.findViewById(R.id.username);
        nameView.setText(username);

        TextView emailView = (TextView) menuHeader.findViewById(R.id.email);
        emailView.setText(email);

        imageUrl = profile.getProfilePictureUri(200,200).toString();
        new DownloadImage((ImageView) menuHeader.findViewById(R.id.menu_profile_image)).execute(imageUrl);
    }

    public void profile(View v) {
        Intent intent = new Intent(getApplicationContext(), UserOwnProfile.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("imageUrl", imageUrl);
        startActivity(intent);
    }
}