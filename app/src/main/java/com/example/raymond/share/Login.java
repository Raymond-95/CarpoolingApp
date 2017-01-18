package com.example.raymond.share;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    private CallbackManager callbackManager;
    private static String email;
    private static String name;
    private static String phonenum;
    private static String profileUrl;
    private static String imageUrl;
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
                        profileUrl = getFacebookPageURL(getApplicationContext());

                        Profile profile = Profile.getCurrentProfile();
                        imageUrl = profile.getProfilePictureUri(200,200).toString();
                        name = profile.getName();

                        phonenum = "0124317038";

                        signUp(email, name, phonenum, profileUrl, imageUrl);

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
            Log.v("LoginActivity", e.getCause().toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        LoginButton loginButton;

        loginButton = (LoginButton)findViewById(R.id.facebookLogin);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, callback);
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

    private void signUp(String email, String name, String phonenum, String profileUrl, String imageUrl){

        mProgressDialog = ShareApi.init(this)
                .registerAccount(
                        email,
                        name,
                        phonenum,
                        profileUrl,
                        imageUrl

                ).call(new ShareApi.DialogResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta) {
                        try {
                            Log.d(TAG, "Sign Up Success.");
                            emailLogin();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta) {
                    }

                })
                .keepProgressDialog()
                .getProgressDialog();
    }

    public void emailLogin() {

        ShareApi.init(getApplicationContext())
                .setProgressDialog(mProgressDialog)
                .emailLogin(
                        email
                ).call(
                new ShareApi.DialogResponseHandler(){

                    @Override
                    public void onSuccess(JSONObject response, ShareJSON meta){

                        setResult(RESULT_OK);
                        getAccount();
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta){

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

                        Intent intent = new Intent(getApplicationContext(), Homepage.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject response, ShareJSON meta){

                    }
                }
        );
    }
}
