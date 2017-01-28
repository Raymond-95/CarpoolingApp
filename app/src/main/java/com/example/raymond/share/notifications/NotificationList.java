package com.example.raymond.share.notifications;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.raymond.share.R;
import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.TripRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationList extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView notificationList;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notifications");

        notificationList = (RecyclerView) findViewById(R.id.notificationList);
        notificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notificationAdapter = new NotificationAdapter();
        notificationList.setAdapter(notificationAdapter);

        getNotifications();

    }

    public void getNotifications() {

        ShareApi.init(getApplicationContext())
                .getNotifications()
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        List<TripRequest> notification = new ArrayList<>();

                        try {

                            for (int i = 0; i < meta.getResults().length(); i++) {

                                notification.add(new TripRequest(meta.getResults().getJSONObject(i)));
                            }

                            notificationAdapter.addData(notification);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {
                        Log.e("Share.TripRequest", e.toString());
                    }

                });
    }
}
