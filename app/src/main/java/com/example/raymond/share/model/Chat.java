package com.example.raymond.share.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Raymond on 28/1/2017.
 */
public class Chat {

    int sender, recipient;
    String message;

    public Chat(){}

    public Chat(int sender, int recipient, String message){
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public Chat(JSONObject response){

        try {
            response = response.getJSONObject("result");

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
}
