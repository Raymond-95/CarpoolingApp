package com.example.raymond.share.tripList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.raymond.share.R;
import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.Trip;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TripHistory extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView tripList;
    private TripAdapter tripAdapter;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Carpools");

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tripList = (RecyclerView) findViewById(R.id.tripList);
        tripList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tripAdapter = new TripAdapter();
        tripList.setAdapter(tripAdapter);

        loadData();
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

    public void loadData() {

        mProgressDialog =ShareApi.init(this)
                .setProgressDialog(mProgressDialog)
                .getHistory()
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        List<Trip> trips = new ArrayList<>();

                        try {

                            for (int i = 0; i < meta.getResults().length(); i++) {

                                trips.add(new Trip(meta.getResults().getJSONObject(i)));

                            }

                            tripAdapter.addData(trips);
                            tripAdapter.getFrom("history");

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
    protected void onStop() {
        super.onStop();

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
