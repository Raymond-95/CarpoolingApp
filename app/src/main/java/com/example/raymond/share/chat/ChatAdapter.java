package com.example.raymond.share.chat;

/**
 * Created by Shade on 5/9/2016.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raymond.share.DownloadImage;
import com.example.raymond.share.R;
import com.example.raymond.share.model.Chat;
import com.example.raymond.share.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chat> mItems;
    private ImageView item_image;
    private TextView item_name;
    private TextView item_message;
    private User user;
    private String lastMessage;

    public ChatAdapter() {
        mItems = new ArrayList<>();
    }

    public void add(Chat data) {
        mItems.add(data);
    }

    public void addData(List<Chat> data) {
        if (data != null) {
            for (Chat appEntry : data) {
                mItems.add(appEntry);
            }

            notifyDataSetChanged();
        }
    }

    public void addUser(User user) {
        this.user = user;
    }

    public void setData(List<Chat> data) {
        mItems.clear();
        if (data != null) {
            for (Chat appEntry : data) {
                mItems.add(appEntry);
            }
        }
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;

        public ViewHolder(View itemView) {
            super(itemView);
            item_image = (ImageView)itemView.findViewById(R.id.profileImage);
            item_name = (TextView)itemView.findViewById(R.id.name);
            item_message = (TextView)itemView.findViewById(R.id.lastMessage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Chat chat = mItems.get(position);

                    if (chat != null) {
                        Intent intent = new Intent(v.getContext(), Message.class);
                        intent.putExtra("id", Integer.toString(chat.getUserId()));
                        v.getContext().startActivity(intent);
                    }

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Chat currentChat = mItems.get(position);

        viewHolder.setIsRecyclable(false);
        item_name.setText(currentChat.getName());
        new DownloadImage(item_image).execute(currentChat.getImageUrl());


        Log.e("asdads",user.getName() );
        if (user.getId() == currentChat.getSender()){
            lastMessage = "You: " + currentChat.getMessage();
        }
        else{
            lastMessage = currentChat.getName() + ": " + currentChat.getMessage();
        }
        item_message.setText(lastMessage);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}