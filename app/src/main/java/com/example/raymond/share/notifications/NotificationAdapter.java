package com.example.raymond.share.notifications;

/**
 * Created by Shade on 5/9/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.raymond.share.DownloadImage;
import com.example.raymond.share.R;
import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.TripRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<TripRequest> mItems;
    private ImageView item_image;
    private TextView item_name;
    private TextView item_body;
    private TextView item_source;
    private TextView item_destination;
    private Button item_ignore;
    private Button item_accept;
    private RelativeLayout layout;
    private View view;
    ProgressDialog mProgressDialog;

    public NotificationAdapter() {
        mItems = new ArrayList<>();
    }

    public void add(TripRequest data) {
        mItems.add(data);
    }

    public void addData(List<TripRequest> data) {
        if (data != null) {
            for (TripRequest appEntry : data) {
                mItems.add(appEntry);

            }

            notifyDataSetChanged();
        }
    }

    public void setData(List<TripRequest> data) {
        mItems.clear();
        if (data != null) {
            for (TripRequest appEntry : data) {
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
            item_image = (ImageView)itemView.findViewById(R.id.profile_pic);
            item_name = (TextView)itemView.findViewById(R.id.name);
            item_body = (TextView)itemView.findViewById(R.id.body);
            item_source = (TextView)itemView.findViewById(R.id.source);
            item_destination = (TextView)itemView.findViewById(R.id.destination);
            item_ignore = (Button) itemView.findViewById(R.id.ignore);
            item_accept = (Button) itemView.findViewById(R.id.accept);

            layout = (RelativeLayout) itemView.findViewById(R.id.segment);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notification_list_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final TripRequest currentRequest = mItems.get(position);

        viewHolder.setIsRecyclable(false);
        item_name.setText(currentRequest.getUserName());
        item_source.setText(currentRequest.getSource());
        item_destination.setText(currentRequest.getDestination());
        new DownloadImage(item_image).execute(currentRequest.getImageUrl());

        if (currentRequest.getStatus().equals("pending")){

            item_body.setText("Hi, I would like to take a ride with you.");

            item_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateTripRequest(currentRequest.getId(), currentRequest.getTripId(), currentRequest.getUserId(), "approved", "not available");
                }
            });

            item_ignore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateTripRequest(currentRequest.getId(), currentRequest.getTripId(), currentRequest.getUserId(), "cancelled", "available");
                }
            });
        }
        else if (currentRequest.getStatus().equals("cancelled")){

            item_body.setText("Hope that we can travel together for the next trip.");

            layout.removeView(item_accept);
            layout.removeView(item_ignore);
        }
        else if (currentRequest.getStatus().equals("approved")){

            item_body.setText("I'm so glad that we can travel together.");

            layout.removeView(item_accept);
            layout.removeView(item_ignore);
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateTripRequest(int id, int trip_id, int requested_by, String status, String trip_status) {

        mProgressDialog = ShareApi.init(view.getContext())
                .setProgressDialog(mProgressDialog)
                .updateTripRequest(id, trip_id, requested_by, status, trip_status)
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                        Log.d("Share", "Request is updated.");
                        ((Activity)view.getContext()).finish();
                        ((Activity)view.getContext()).startActivity(new Intent(view.getContext(), NotificationList.class));
                    }
                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {
                        Log.e("Share", e.toString());
                    }

                })
                .keepProgressDialog()
                .getProgressDialog();
    }
}