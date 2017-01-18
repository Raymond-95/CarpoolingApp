package com.example.raymond.share.tripList;

/**
 * Created by Shade on 5/9/2016.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raymond.share.DownloadImage;
import com.example.raymond.share.R;
import com.example.raymond.share.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private List<Trip> mItems;
    private ImageView item_image;
    private TextView item_name;
    private TextView item_source;
    private TextView item_destination;
    private TextView item_date;
    private String from;

    public TripAdapter() {
        mItems = new ArrayList<>();
    }

    public void add(Trip data) {
        mItems.add(data);
    }

    public void addData(List<Trip> data) {
        if (data != null) {
            for (Trip appEntry : data) {
                mItems.add(appEntry);
            }

            notifyDataSetChanged();
        }
    }

    public void getFrom(String from) {
        this.from = from;
    }

    public void setData(List<Trip> data) {
        mItems.clear();
        if (data != null) {
            for (Trip appEntry : data) {
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
            item_image = (ImageView)itemView.findViewById(R.id.profilePic);
            item_name = (TextView)itemView.findViewById(R.id.item_name);
            item_source = (TextView)itemView.findViewById(R.id.item_source);
            item_destination = (TextView)itemView.findViewById(R.id.item_destination);
            item_date = (TextView)itemView.findViewById(R.id.item_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Trip trip = mItems.get(position);

                    if (trip != null) {
                        Intent intent = new Intent(v.getContext(), TripDetail.class);
                        intent.putExtra("id", Integer.toString(trip.getId()));
                        intent.putExtra("from", from);
                        v.getContext().startActivity(intent);
                    }

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Trip currentTrip = mItems.get(position);

        viewHolder.setIsRecyclable(false);
        item_name.setText(currentTrip.getName());
        item_source.setText(currentTrip.getSource());
        item_destination.setText(currentTrip.getDestination());
        item_date.setText(currentTrip.getDate());
        new DownloadImage(item_image).execute(currentTrip.getImageUrl());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}