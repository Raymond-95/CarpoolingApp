package com.example.raymond.share.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Raymond on 28/1/2017.
 */
public class Chat {

    int sender, recipient, mRecipientOrSenderStatus, user_id;
    String message, name, imageUrl;

    public Chat(){}

    public Chat(int sender, int recipient, String message){
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public Chat(int user_id, String name, String imageUrl, int sender, String message){
        this.user_id = user_id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.sender = sender;
        this.message = message;
    }

    public Chat(JSONObject response){

        try {
            response = response.getJSONObject("result");

            try {
                this.user_id = response.getInt("id");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "User id is null");
            }

            try {
                this.name = response.getString("name");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Name is null");
            }

            try {
                this.imageUrl = response.getString("imageUrl");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "ImageUrl is null");
            }

            try {
                this.sender = response.getInt("sender");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Sender is null");
            }

            try {
                this.recipient = response.getInt("recipient");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Recipient is null");
            }

            try {
                this.message = response.getString("message");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Message is null");
            }

        } catch (JSONException ex) {

            try {
                this.user_id = response.getInt("id");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "User id is null");
            }

            try {
                this.name = response.getString("name");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Name is null");
            }

            try {
                this.imageUrl = response.getString("imageUrl");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "ImageUrl is null");
            }

            try {
                this.sender = response.getInt("sender");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Sender is null");
            }

            try {
                this.recipient = response.getInt("recipient");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Recipient is null");
            }

            try {
                this.message = response.getString("message");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Message is null");
            }
        }
    }

    public int getSender() {
        return sender;
    }

    public int getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setRecipientOrSenderStatus(int recipientOrSenderStatus) {
        this.mRecipientOrSenderStatus = recipientOrSenderStatus;
    }

    public int getRecipientOrSenderStatus() {
        return mRecipientOrSenderStatus;
    }

    public int getUserId() {return user_id;}

    public String getName() {return name;}

    public String getImageUrl() {return imageUrl;}
}
