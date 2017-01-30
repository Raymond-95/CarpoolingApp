package com.example.raymond.share.chat;

/**
 * Created by Shade on 5/9/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.raymond.share.R;
import com.example.raymond.share.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Chat> mItems;
    private TextView item_message;
    public MessageAdapter() {
        mItems = new ArrayList<>();
    }
    private int myId;
    private Context mContext;

    public void addContext(Context context){
        this.mContext = context;
    }

    public void add(Chat data) {
        mItems.add(data);
    }

    public void addData(List<Chat> data, int id) {
        if (data != null) {
            for (Chat appEntry : data) {
                mItems.add(appEntry);
            }

            notifyDataSetChanged();
        }
        this.myId = id;
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
            item_message = (TextView)itemView.findViewById(R.id.single_message);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Chat chat = mItems.get(position);

        viewHolder.setIsRecyclable(false);

        if (myId != chat.getSender()){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 60, 2); // llp.setMargins(left, top, right, bottom);
            item_message.setLayoutParams(params);
            item_message.setTextColor(mContext.getResources().getColor(R.color.dark_grey));
            item_message.setBackground(mContext.getResources().getDrawable(R.drawable.recipient_rounded_corners));
        }

        item_message.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}