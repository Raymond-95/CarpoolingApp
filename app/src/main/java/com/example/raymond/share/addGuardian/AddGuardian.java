package com.example.raymond.share.addGuardian;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.raymond.share.R;
import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.Guardian;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddGuardian extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView guardianList;
    private GuardianAdapter guardianAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guardian);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Guardians");

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        guardianList = (RecyclerView) findViewById(R.id.chatusers);
        guardianList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        guardianAdapter = new GuardianAdapter();
        guardianList.setAdapter(guardianAdapter);

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
        ShareApi.init(this)
                .getGuardians()
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        List<Guardian> guardians = new ArrayList<>();

                        try {

                            for (int i = 0; i < meta.getResults().length(); i++) {

                                guardians.add(new Guardian(meta.getResults().getJSONObject(i)));

                            }

                            guardianAdapter.addData(guardians);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {
                        Log.e("Share.ChatUser", "Cannot retrieve data");
                    }

                });
    }
}
