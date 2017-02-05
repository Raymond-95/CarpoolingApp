package com.example.raymond.share.tripList.verifyLicense;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.raymond.share.R;

public class LicenseInfo extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView licenseInfo;
    private LicenseAdapter licenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_info);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Driving License Details");

        Intent intent = getIntent();
        String [] license_info = intent.getStringArrayExtra("license_info");

        licenseInfo = (RecyclerView) findViewById(R.id.licenseInfo);
        licenseInfo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        licenseAdapter = new LicenseAdapter();
        licenseInfo.setAdapter(licenseAdapter);

        licenseAdapter.addData(license_info);
        licenseAdapter.addContext(getApplicationContext());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
