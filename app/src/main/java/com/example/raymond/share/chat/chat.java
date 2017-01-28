//package com.example.raymond.share.chat;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import com.example.raymond.share.R;
//import com.example.raymond.share.jsonparser.ShareApi;
//import com.example.raymond.share.jsonparser.ShareJSON;
//import com.example.raymond.share.model.Trip;
//import com.example.raymond.share.model.TripRequest;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Chat extends AppCompatActivity {
//
//    private Toolbar toolbar;
//    private RecyclerView chatList;
//    private ChatAdapter chatAdapter;
//    ProgressDialog mProgressDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notification_list);
//
//        // Initializing Toolbar and setting it as the actionbar
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("Message");
//
//        chatList = (RecyclerView) findViewById(R.id.notificationList);
//        chatList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        chatAdapter = new ChatAdapter();
//        chatList.setAdapter(chatAdapter);
//
//        loadData();
//
//    }
//
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_card_demo, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void loadData() {
//
//        mProgressDialog = ShareApi.init(this)
//                .setProgressDialog(mProgressDialog)
//                .getTrips("driver")
//                .call(new ShareApi.CustomJsonResponseHandler() {
//
//                    @Override
//                    public void onSuccess(JSONObject response, ShareJSON meta) {
//
//                        List<Trip> drive = new ArrayList<>();
//
//                        try {
//
//                            for (int i = 0; i < meta.getResults().length(); i++) {
//
//                                drive.add(new Trip(meta.getResults().getJSONObject(i)));
//
//                            }
//
//                            chatAdapter.addData(drive);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {
//
//                    }
//
//                })
//                .keepProgressDialog()
//                .getProgressDialog();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//            mProgressDialog = null;
//        }
//    }
//}
