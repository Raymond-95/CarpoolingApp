package com.example.raymond.share.jsonparser;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONObject;

import java.io.File;

public class ShareApi {

    private Context context;
    private ShareRestClient client;

    private ShareApi(Builder builder){
        this.context = builder.mContext;
        this.client = builder.mClient;
    }

    public interface DialogResponseHandler {
        void onSuccess(JSONObject response, ShareJSON meta);
        void onFailure(Throwable throwable, JSONObject response, ShareJSON meta);
    }

    public interface CustomJsonResponseHandler {
        void onSuccess(JSONObject response, ShareJSON meta);
        void onFailure(Throwable throwable, JSONObject response, ShareJSON meta);
    }

    public static Builder init(Context context) {
        return new ShareApi.Builder(context);
    }

    public static class Builder{

        private Context mContext;
        private ShareRestClient mClient;

        public Builder(Context context){
            mContext = context;
            mClient = new ShareRestClient(context);
        }

        public Builder call(DialogResponseHandler responseHandler){

            //mClient.addParam("key", Muralink.MURALINK_KEY);
            mClient.request(responseHandler);
            return this;
        }

        public Builder call(CustomJsonResponseHandler responseHandler){

           // mClient.addParam("key", Muralink.MURALINK_KEY);
            mClient.request(responseHandler);
            return this;
        }

        public Builder keepProgressDialog(){
            mClient.keepProgressDialog();
            return this;
        }

        public Builder onSuccessDialogClose(ShareRestClient.Command command){
            mClient.onSuccessDialogClose(command);
            return this;
        }
        public Builder onFailureDialogClose(ShareRestClient.Command command){
            mClient.onFailureDialogClose(command);
            return this;
        }

        public ProgressDialog getProgressDialog(){
            return mClient.getProgressDialog();
        }

        public Builder setProgressDialog(ProgressDialog progressDialog){
            mClient.setProgressDialog(progressDialog);
            return this;
        }

        public Builder showMessageDialog(boolean show){
            mClient.showMessageDialog(show);
            return this;
        }

        public Builder finishWhenClickedOKButton(){
            mClient.setFinishActivity(true);
            return this;
        }

        public Builder emailLogin(
                String email
        ){

            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/login");

            mClient.addParam("email", email);
            mClient.addParam("password", email);

            return this;
        }
        
        public Builder facebookLogin(
                String token
        ){
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/facebook/connect");
            
            mClient.addParam("token", token);

            return this;
        }

        public Builder logout(){

            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/logout");

            return this;
        }

        public Builder registerAccount(
                String email,
                String name,
                String profileUrl,
                String imageUrl
        ){
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/signup");

            mClient.addParam("email", email);
            mClient.addParam("name", name);
            mClient.addParam("profileUrl", profileUrl);
            mClient.addParam("imageUrl", imageUrl);

            return this;
        }

        public Builder getAccount(){
            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/accounts/me");

            return this;
        }

        public Builder registerTrip(
                String source,
                String destination,
                String date,
                String time,
                String role,
                String information
        ){
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/trip/register_trip");

            mClient.addParam("source", source);
            mClient.addParam("destination", destination);
            mClient.addParam("date", date);
            mClient.addParam("time", time);
            mClient.addParam("role", role);
            mClient.addParam("information", information);

            return this;
        }

        public Builder getTrip(){
            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/trip/get_driver");

            return this;
        }

        public Builder updateAccount(
                String fname,
                String lname,
                String gender,
                String dob,
                String mobile,
                String email,
                File picture
        ){
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/me/update");

            mClient.addParam("first_name", fname);
            mClient.addParam("last_name", lname);
            mClient.addParam("gender", gender);
            mClient.addParam("dob", dob);
            mClient.addParam("mobile", mobile);
            mClient.addParam("email", email);
            mClient.addParam("image", picture);

            return this;
        }

        public Builder updatePassword(
                String current_pass,
                String new_pass,
                String retype_new_pass
        ){
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/passwords/change");

            mClient.addParam("current_password", current_pass);
            mClient.addParam("new_password", new_pass);
            mClient.addParam("confirm_password", retype_new_pass);

            return this;
        }

        public Builder resetPassword(
                String email
        ){
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/passwords/reset");

            mClient.addParam("email", email);

            return this;
        }
        
        public Builder getPlaces(
                String view,
                String query,
        		double lat,
        		double lng,
                float radius,
                int page
		){

            mClient.setExitWhen401(false);

            mClient.addParam("view", view);
            mClient.addParam("q", query);
        	mClient.addParam("lat", lat);
    		mClient.addParam("lng", lng);
            mClient.addParam("radius", radius);
            mClient.addParam("page", page);

            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/locations");

            return this;
        }

        public Builder getPost(
                int id
        ){

            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/posts/"+id);


            return this;
        }

        public Builder getPosts(){

            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/posts");

            return this;
        }


        public Builder getMyPost(){

            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/posts/me");

            return this;
        }

        public Builder getPlaceDetails(
        		int id,
        		double lat,
        		double lng
		){

            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/locations/" + id);

        	mClient.addParam("lat", lat);
    		mClient.addParam("lng", lng);
    		//mClient.addParam("campaign_id", Muralink.CAMPAIGN_ID);

            return this;
        }

        public Builder checkIn(
        		int placeId,
        		double lat,
        		double lng,
        		String comment,
        		File photo,
        		String facebookToken,
        		String twitterToken,
        		String twitterSecret
		){

            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/places/" + placeId + "/check/in");
            
            if(facebookToken != null){
    			mClient.addParam("facebook_token", facebookToken);
    		}
    		if(twitterToken != null && twitterSecret != null){
    			mClient.addParam("twitter_token", twitterToken);
    			mClient.addParam("twitter_secret", twitterSecret);
    		}
            
            //mClient.addParam("lat", 5.421386);
    		//mClient.addParam("lng", 100.333);
    		mClient.addParam("lat", lat);
    		mClient.addParam("lng", lng);
    		mClient.addParam("campaign_id", Share.CAMPAIGN_ID);
    		mClient.addParam("comment", comment);
    		mClient.addParam("photo", photo);
    		mClient.addParam("maker", Share.MAKER);
    		mClient.addParam("model", Share.MODEL);

            return this;
        }

        public Builder addPost(
                int user_id,
                String desc,
                File image,
                double geo_lat,
                double geo_lng

        ){

            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/posts/add");

            mClient.addParam("user_id", user_id);
            mClient.addParam("description", desc);
            mClient.addParam("image", image);
            mClient.addParam("geo_lat", geo_lat);
            mClient.addParam("geo_lng", geo_lng);

            return this;
        }
        
        public Builder registerDevice(
        		String guid,
        		String token
		){

            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/devices/me/register");

    		mClient.addParam("guid", guid);
    		mClient.addParam("token", token);
    		mClient.addParam("platform", Share.PLATFORM);
    		mClient.addParam("maker", Share.MAKER);
    		mClient.addParam("model", Share.MODEL);

            return this;
        }
    }

}

