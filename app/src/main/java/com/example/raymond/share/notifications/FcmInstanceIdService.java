package com.example.raymond.share.notifications;

import android.util.Log;

import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

/**
 * Created by Raymond on 23/1/2017.
 */
public class FcmInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseInsIDService";

    @Override
    public void onTokenRefresh() {
        //Get updated token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "New Token: " + refreshedToken);

        updateToken(refreshedToken);

        //You can save the token into third party server to do anything you want
    }

    public void updateToken(String token) {

        ShareApi.init(getApplicationContext())
                .updateToken(
                        token
                ).call(
                new ShareApi.DialogResponseHandler(){

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta){

                        Log.e(TAG, "Token is successfully updated.");
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta){

                    }
                }
        );
    }
}
