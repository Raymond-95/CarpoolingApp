package com.example.raymond.share.model;

import android.content.Context;
import android.util.Log;

import com.example.raymond.share.jsonparser.Share;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiaminglow on 4/19/16.
 */

import org.parceler.Parcel;

@Parcel
public class User {
    int id;
    String email, name, phonenum, profileUrl, imageUrl, token;

    public User(){}

    public User(int id, String email, String name, String phonenum, String profileUrl, String imageUrl, String token){
        this.id = id;
        this.email = email;
        this.name = name;
        this.phonenum = phonenum;
        this.profileUrl = profileUrl;
        this.imageUrl = imageUrl;
        this.token = token;
    }

    public User(JSONObject response){

        try {
            response = response.getJSONObject("result");

            try {
                this.id = response.getInt("id");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "id is null");
            }

            try {
                this.email = response.getString("email");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "email is null");
            }

            try {
                this.name = response.getString("name");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Name is null");
            }

            try {
                this.phonenum = response.getString("phonenum");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Phone number is null");
            }

            try {
                this.profileUrl = response.getString("profileUrl");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Profile url is null");
            }

            try {
                this.imageUrl = response.getString("imageUrl");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Image url is null");
            }

            try {
                this.token = response.getString("token");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "token is null");
            }

        } catch (JSONException ex) {

            try {
                this.id = response.getInt("id");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "id is null");
            }

            try {
                this.email = response.getString("email");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "email is null");
            }

            try {
                this.name = response.getString("name");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Name is null");
            }

            try {
                this.phonenum = response.getString("phonenum");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Phone number is null");
            }

            try {
                this.profileUrl = response.getString("profileUrl");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Profile url is null");
            }

            try {
                this.imageUrl = response.getString("imageUrl");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Image url is null");
            }

            try {
                this.token = response.getString("token");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "token is null");
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() { return name; }
    public void setname(String name) {
        this.name = name;
    }

    public String getPhonenum() { return phonenum; }
    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getProfileUrl() { return profileUrl; }
    public void setProfileUrl(String profileUrl) { this.profileUrl = profileUrl; }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getToken() {return token; }
    public void setToken(String token) { this.token = token; }

    public void saveUserAccount(Context context){

        Share.AppPreferences appPreferences = new Share.AppPreferences(context);
        appPreferences.saveUserId(id);
        appPreferences.saveUserEmail(email);
        appPreferences.saveUsername(name);
        appPreferences.saveUserPhonenum(phonenum);
        appPreferences.saveUserProfileUrl(profileUrl);
        appPreferences.saveUserImageUrl(imageUrl);
        appPreferences.saveUserToken(token);
    }

    public User getUserAccount(Context context){

        Share.AppPreferences appPreferences = new Share.AppPreferences(context);
        this.id = appPreferences.getUserId();
        this.email = appPreferences.getUserEmail();
        this.name = appPreferences.getUsername();
        this.phonenum = appPreferences.getUserPhonenum();
        this.profileUrl = appPreferences.getUserProfileUrl();
        this.imageUrl = appPreferences.getUserImageUrl();
        this.token = appPreferences.getUserToken();

        return this;
    }

    public void removeUserAccount(Context context){

        Share.AppPreferences appPreferences = new Share.AppPreferences(context);
        appPreferences.saveUserId(0);
        appPreferences.saveUserEmail("");
        appPreferences.saveUsername("");
        appPreferences.saveUserPhonenum("");
        appPreferences.saveUserProfileUrl("");
        appPreferences.saveUserImageUrl("");
        appPreferences.saveUserToken("");
    }
}
