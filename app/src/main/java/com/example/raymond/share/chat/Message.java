package com.example.raymond.share.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.raymond.share.DownloadImage;
import com.example.raymond.share.R;
import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.Chat;
import com.example.raymond.share.model.User;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView messageList;
    private MessageAdapter messageAdapter;
    private int id;
    private int size = 0;
    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String pass_id = getIntent().getStringExtra("id");
        id = Integer.parseInt(pass_id);

        messageList = (RecyclerView) findViewById(R.id.recycler_view_chat);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messageList.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(new ArrayList<Chat>());
        messageList.setAdapter(messageAdapter);

        getUser(id);

        final EditText message = (EditText) findViewById(R.id.message_text);
        Button send_message = (Button) findViewById(R.id.btn_send_message);

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = message.getText().toString();
                if (!text.isEmpty()){
                    sendMessage(id, message.getText().toString());
                    message.setText("");
                    getMessage(id);
                }
            }
        });

        getMessage(id);
    }

    public void getUser(int id) {

        ShareApi.init(getApplicationContext())
                .getUser(id)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        user = new User(meta.getResult());

                        CircleImageView profilePic = (CircleImageView) findViewById(R.id.profileImage);
                        new DownloadImage(profilePic).execute(user.getImageUrl());

                        TextView name = (TextView) findViewById(R.id.name);
                        name.setText(user.getName());

                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {

                    }

                });
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

                        ArrayList<Chat> messages = new ArrayList<>();

                        try {

                            for (int i = 0; i < meta.getResults().length(); i++) {

                                messages.add(new Chat(meta.getResults().getJSONObject(i)));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (messages.size() >= size){
                            for (int i = size; i < messages.size(); i++) {

                                User me = new User().getUserAccount(getApplicationContext());
                                Chat newMessage = messages.get(i);

                                if(newMessage.getSender() == me.getId()){
                                    newMessage.setRecipientOrSenderStatus(MessageAdapter.SENDER);
                                }else{
                                    newMessage.setRecipientOrSenderStatus(MessageAdapter.RECIPIENT);
                                }
                                messageAdapter.refillAdapter(newMessage);
                                messageList.scrollToPosition(messageAdapter.getItemCount()-1);

                            }
                        }
                        size = messages.size();
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {

                    }

                });
    }

    @Override
    protected void onStop() {
        super.onStop();

        messageAdapter.cleanUp();

    }
}
