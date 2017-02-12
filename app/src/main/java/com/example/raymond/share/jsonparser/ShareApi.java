package com.example.raymond.share.jsonparser;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONObject;

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

    public static class Builder {

        private Context mContext;
        private ShareRestClient mClient;

        public Builder(Context context) {
            mContext = context;
            mClient = new ShareRestClient(context);
        }

        public Builder call(DialogResponseHandler responseHandler) {

            //mClient.addParam("key", Share.SHARE_KEY);
            mClient.request(responseHandler);
            return this;
        }

        public Builder call(CustomJsonResponseHandler responseHandler) {

            // mClient.addParam("key", Muralink.MURALINK_KEY);
            mClient.request(responseHandler);
            return this;
        }

        public Builder keepProgressDialog() {
            mClient.keepProgressDialog();
            return this;
        }

        public Builder onSuccessDialogClose(ShareRestClient.Command command) {
            mClient.onSuccessDialogClose(command);
            return this;
        }

        public Builder onFailureDialogClose(ShareRestClient.Command command) {
            mClient.onFailureDialogClose(command);
            return this;
        }

        public ProgressDialog getProgressDialog() {
            return mClient.getProgressDialog();
        }

        public Builder setProgressDialog(ProgressDialog progressDialog) {
            mClient.setProgressDialog(progressDialog);
            return this;
        }

        public Builder showMessageDialog(boolean show) {
            mClient.showMessageDialog(show);
            return this;
        }

        public Builder finishWhenClickedOKButton() {
            mClient.setFinishActivity(true);
            return this;
        }

        public Builder emailLogin(
                String email
        ) {

            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/login");

            mClient.addParam("email", email);
            mClient.addParam("password", email);

            return this;
        }

        public Builder logout() {

            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/logout");

            return this;
        }

        public Builder registerAccount(
                String email,
                String name,
                String phonenum,
                String profileUrl,
                String imageUrl
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/signup");

            mClient.addParam("email", email);
            mClient.addParam("name", name);
            mClient.addParam("phonenum", phonenum);
            mClient.addParam("profileUrl", profileUrl);
            mClient.addParam("imageUrl", imageUrl);

            return this;
        }

        public Builder getExistingAcc(
                String email
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/get_existing_user");

            mClient.addParam("email", email);

            return this;
        }

        public Builder getAccount() {
            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/accounts/me");

            return this;
        }

        public Builder getUser(
                int id
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/get_user");

            mClient.addParam("id", id);

            return this;
        }

        public Builder updateUser(
                String email,
                String name,
                String phonenum,
                String profileUrl,
                String imageUrl
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/update_user");

            mClient.addParam("email", email);
            mClient.addParam("name", name);
            mClient.addParam("phonenum", phonenum);
            mClient.addParam("profileUrl", profileUrl);
            mClient.addParam("imageUrl", imageUrl);

            return this;
        }

        public Builder updateRole()
        {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/accounts/update_role");

            return this;
        }

        public Builder registerTrip(
                String source,
                String destination,
                String date,
                String time,
                String role,
                String information
        ) {
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

        public Builder updateTrip(
                int id,
                String source,
                String destination,
                String date,
                String time,
                String role,
                String information
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/trip/update_trip/" + id);

            mClient.addParam("source", source);
            mClient.addParam("destination", destination);
            mClient.addParam("date", date);
            mClient.addParam("time", time);
            mClient.addParam("role", role);
            mClient.addParam("information", information);

            return this;
        }

        public Builder getTrips(
                String role
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/trip/get_trips");

            mClient.addParam("role", role);

            return this;
        }

        public Builder getTrip(
                int id
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/trip/get_trip_details");

            mClient.addParam("id", id);

            return this;
        }

        public Builder getHistory() {
            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/trip/get_history");

            return this;
        }

        public Builder sendRequest(
                int id,
                String type
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/notification/send_request");

            mClient.addParam("id", id);
            mClient.addParam("type", type);

            return this;
        }

        public Builder updateTripRequest(
                int id,
                int trip_id,
                int requested_by,
                String status,
                String trip_status
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/trip/update_trip_request/" + id);

            mClient.addParam("id", id);
            mClient.addParam("trip_id", trip_id);
            mClient.addParam("requested_by", requested_by);
            mClient.addParam("status", status);
            mClient.addParam("trip_status", trip_status);

            return this;
        }

        public Builder storeToken(
                String email,
                String fcmtoken
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/notification/store_token");

            mClient.addParam("email", email);
            mClient.addParam("token", fcmtoken);

            return this;
        }

        public Builder updateToken(
                String token
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/notification/update_token");

            mClient.addParam("token", token);

            return this;
        }

        public Builder getNotifications() {
            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/notification/get_notifications");

            return this;
        }

        public Builder sendMessage(
                int id,
                String message
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/chat/send_message");

            mClient.addParam("id", id);
            mClient.addParam("message", message);

            return this;
        }

        public Builder getMessage(
                int id
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/chat/get_message");

            mClient.addParam("id", id);

            return this;
        }

        public Builder getChatUsers() {
            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/chat/get_chat_users");

            return this;
        }

        public Builder updateGuardian(
                int id,
                int trip_id,
                int guardian,
                String status
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/guardian/update_guardian/" + id);

            mClient.addParam("id", id);
            mClient.addParam("trip_id", trip_id);
            mClient.addParam("guardian", guardian);
            mClient.addParam("status", status);

            return this;
        }

        public Builder getGuardians() {
            mClient.setMethod(ShareRestClient.GET);
            mClient.setEndPoint("/guardian/get_guardians");

            return this;
        }

        public Builder search(
                String source,
                String destination,
                String role
        ) {
            mClient.setExitWhen401(false);
            mClient.setMethod(ShareRestClient.POST);
            mClient.setEndPoint("/trip/search");

            mClient.addParam("source", source);
            mClient.addParam("destination", destination);
            mClient.addParam("role", role);

            return this;
        }
    }
}

