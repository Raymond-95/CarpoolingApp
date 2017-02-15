package com.example.raymond.share;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.raymond.share.jsonparser.ShareApi;
import com.example.raymond.share.jsonparser.ShareJSON;
import com.example.raymond.share.model.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    private CallbackManager callbackManager;
    private static String email;
    private String finalResult = "false";
    private Button facebookLogin;
    private LoginButton loginButton;
    ProgressDialog mProgressDialog;
    private static final String TAG = "share.activity.login";


    //Facebook login button
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {

            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.i("LoginActivity", response.toString());
                    try {
                        email = object.getString("email");
                        String profileUrl = getFacebookPageURL(getApplicationContext());

                        Profile profile = Profile.getCurrentProfile();
                        String imageUrl = profile.getProfilePictureUri(200,200).toString();
                        String name = profile.getName();

                        String phonenum = "0124317038";

                        getExistingAcc(email);

                        if (finalResult.equals("false")){

                            //store FCM token if it is a first time login
                            String fcmtoken = FirebaseInstanceId.getInstance().getToken();
                            Log.e(TAG, "Token: " + fcmtoken);
                            storeToken(email, fcmtoken);
                        }

                        registerAccount(email, name, phonenum, profileUrl, imageUrl);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "email"); // Parámetros que pedimos a facebook
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            startActivity(new Intent(getApplicationContext(), Login.class));
            Toast.makeText(getApplicationContext(), "Login has been cancelled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(getApplicationContext(), "Error has been encountered", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        LoginManager.getInstance().logOut();

        facebookLogin = (Button) findViewById(R.id.facebookLogin);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, callback);
    }

    public void onClick(View v) {
        if (v == facebookLogin) {
            loginButton.performClick();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);

    }

    public String getFacebookPageURL(Context context) {

        Profile userId = Profile.getCurrentProfile();

        String FACEBOOK_PAGE_ID = userId.getId();
        String FACEBOOK_URL = "https://www.facebook.com/" + FACEBOOK_PAGE_ID;

        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public void getExistingAcc(String email) {

        mProgressDialog =ShareApi.init(this)
                .setProgressDialog(mProgressDialog)
                .getExistingAcc(
                        email
                ).call(
                        new ShareApi.DialogResponseHandler(){

                            @Override
                            public void onSuccess(JSONObject response, ShareJSON meta){

                                String result;
                                try {
                                    result = response.getJSONObject("result").getString("check");
                                    finalResult = result;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Throwable e, JSONObject response, ShareJSON meta){
                                Log.e(TAG,"Error on get existing user");
                            }
                        }
                )
                .keepProgressDialog()
                .getProgressDialog();

    }

    public void registerAccount(String email, String name, String phonenum, String profileUrl, String imageUrl) {

        mProgressDialog =ShareApi.init(this)
                .setProgressDialog(mProgressDialog)
                .registerAccount(
                        email,
                        name,
                        phonenum,
                        profileUrl,
                        imageUrl
                        ).call(
                        new ShareApi.DialogResponseHandler(){

                            @Override
                            public void onSuccess(JSONObject response, ShareJSON meta){
                                emailLogin();
                            }

                            @Override
                            public void onFailure(Throwable e, JSONObject response, ShareJSON meta){
                                Log.e("Error", "Registration failed!");
                            }
                        }
                )
                .keepProgressDialog()
                .getProgressDialog();
    }

    public void emailLogin() {

       ShareApi.init(this)
                .emailLogin(
                        email
                ).call(
                new ShareApi.DialogResponseHandler(){

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta){

                        getAccount();
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta){
                        Log.e("Error", "Login failed!");
                    }
                }
        );
    }

    public void getAccount(){
        ShareApi.init(this)
                .getAccount().call(
                new ShareApi.DialogResponseHandler(){

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta){

                        User userInfo = new User(response);
                        userInfo.saveUserAccount(Login.this);
                        setResult(RESULT_OK);

                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }

                        Intent intent = new Intent(getApplicationContext(), Homepage.class);
                        startActivity(intent);

                        finish();
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta){
                        Log.e("GetAccount", "Unable to get user account.");
                    }
                }
        );
    }

    public void storeToken(String email, String fcmtoken) {

        mProgressDialog =ShareApi.init(this)
                .setProgressDialog(mProgressDialog)
                .storeToken(
                        email,
                        fcmtoken
                ).call(
                new ShareApi.DialogResponseHandler(){

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta){

                        Log.d(TAG, "Token is successfully stored.");
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta){
                        Log.e(TAG,"Error on token storing");
                    }
                }
        )
        .keepProgressDialog()
        .getProgressDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        email = null;
    }
}
