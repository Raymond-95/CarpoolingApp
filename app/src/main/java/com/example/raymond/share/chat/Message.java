package com.example.raymond.share.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.raymond.share.R;
import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.Chat;
import com.example.raymond.share.model.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Message extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView messageList;
    private MessageAdapter messageAdapter;
    private int id;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Message");

        String pass_id = getIntent().getStringExtra("id");
        id = Integer.parseInt(pass_id);

        messageList = (RecyclerView) findViewById(R.id.recycler_view_chat);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messageList.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter();
        messageList.setAdapter(messageAdapter);

        final User user = new User().getUserAccount(getApplicationContext());

        final EditText message = (EditText) findViewById(R.id.message_text);
        Button send_message = (Button) findViewById(R.id.btn_send_message);

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Chat newChat = new Chat(user.getId(), id, message.getText().toString());

                sendMessage(id, message.getText().toString());
                messageAdapter.add(newChat);
                messageList.scrollToPosition(messageAdapter.getItemCount() - 1);
                message.setText("");
            }
        });

        getMessage(id);
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

    public void sendMessage(int id, String message) {

        ShareApi.init(this)
                .sendMessage(id, message)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {

                    }

                });
    }

    public void getMessage(int id) {

        ShareApi.init(this)
                .getMessage(id)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        List<Chat> messages = new ArrayList<>();

                        try {

                            for (int i = 0; i < meta.getResults().length(); i++) {

                                messages.add(new Chat(meta.getResults().getJSONObject(i)));

                            }

                            size = messages.size();

                            User user = new User().getUserAccount(getApplicationContext());
                            messageAdapter.addData(messages, user.getId());
                            messageAdapter.addContext(getApplicationContext());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {

                    }

                });
    }
}
