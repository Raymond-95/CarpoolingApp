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
import android.widget.Toast;

import com.example.raymond.share.R;
import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.Trip;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchList extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView searchList;
    private SearchAdapter searchAdapter;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Results");

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchList = (RecyclerView) findViewById(R.id.searchList);
        searchList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchAdapter = new SearchAdapter();
        searchList.setAdapter(searchAdapter);

        String source = getIntent().getStringExtra("source");
        String destination = getIntent().getStringExtra("destination");
        String role = getIntent().getStringExtra("role");

        loadData(source, destination, role);
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

    public void loadData(String source, String destination, String role) {

        mProgressDialog =ShareApi.init(this)
                .setProgressDialog(mProgressDialog)
                .search(source, destination, role)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        List<Trip> trips = new ArrayList<>();

                        try {

                            for (int i = 0; i < meta.getResults().length(); i++) {

                                trips.add(new Trip(meta.getResults().getJSONObject(i)));
                            }

                            if (!trips.isEmpty()){
                                searchAdapter.addData(trips);
                                searchAdapter.getFrom("fragment");
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "No result", Toast.LENGTH_SHORT).show();
                            }

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
