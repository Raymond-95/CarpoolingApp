package com.example.raymond.share.addGuardian;

/**
 * Created by Shade on 5/9/2016.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raymond.share.DownloadImage;
import com.example.raymond.share.R;
import com.example.raymond.share.model.Guardian;

import java.util.ArrayList;
import java.util.List;

public class GuardianAdapter extends RecyclerView.Adapter<GuardianAdapter.ViewHolder> {

    private List<Guardian> mItems;
    private ImageView item_image;
    private TextView item_name;

    public GuardianAdapter() {
        mItems = new ArrayList<>();
    }

    public void add(Guardian data) {
        mItems.add(data);
    }

    public void addData(List<Guardian> data) {
        if (data != null) {
            for (Guardian appEntry : data) {
                mItems.add(appEntry);
            }

            notifyDataSetChanged();
        }
    }

    public void setData(List<Guardian> data) {
        mItems.clear();
        if (data != null) {
            for (Guardian appEntry : data) {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Guardian chat = mItems.get(position);

                    if (chat != null) {
//                        Intent intent = new Intent(v.getContext(), Message.class);
//                        intent.putExtra("id", Integer.toString(chat.getUserId()));
//                        v.getContext().startActivity(intent);
                    }

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.guardian, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Guardian currentGuardian = mItems.get(position);

        viewHolder.setIsRecyclable(false);
        item_name.setText(currentGuardian.getName());
        new DownloadImage(item_image).execute(currentGuardian.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}