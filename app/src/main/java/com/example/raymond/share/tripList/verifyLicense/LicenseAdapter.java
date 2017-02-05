package com.example.raymond.share.tripList.verifyLicense;

/**
 * Created by Shade on 5/9/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.raymond.share.R;
import com.example.raymond.share.RegTrip;
import com.example.raymond.share.jsonparser.Share;
import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;

import org.json.JSONObject;

public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder> {

    private String [] mItems;
    private TextView item_title;
    private TextView item_content;
    private Button next;
    private RelativeLayout layout;
    private static Context mContext;
    private String[] title = {"Name", "IC number", "ID Category", "Type of Driving License", "License Class", "Date of commencement", "Expiry Date"};

    public LicenseAdapter() {
        mItems = new String[7];
    }

    public void addData(String[] data) {

        this.mItems = data;
    }

    public void addContext(Context context) {

        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;

        public ViewHolder(View itemView) {
            super(itemView);
            item_title = (TextView)itemView.findViewById(R.id.item_title);
            item_content = (TextView)itemView.findViewById(R.id.item_content);
            next = (Button) itemView.findViewById(R.id.next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateRole();
                    Share.AppPreferences appPreferences = new Share.AppPreferences(mContext);
                    appPreferences.saveUserRole("driver");

                    Intent intent = new Intent(mContext, RegTrip.class);
                    intent.putExtra("role", "driver");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
            layout = (RelativeLayout) itemView.findViewById(R.id.license_layout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.license_details, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.setIsRecyclable(false);
        item_title.setText( title[position]);
        item_content.setText( mItems[position]);

        layout.removeView(next);

        if(position == 6){
            layout.addView(next);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.length;
    }

    public void updateRole() {

        ShareApi.init(mContext)
                .updateRole()
                .call(new ShareApi.CustomJsonResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {

                    }
                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {

                    }

                });
    }
}