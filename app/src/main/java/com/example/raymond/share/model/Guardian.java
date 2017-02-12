package com.example.raymond.share.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Raymond on 28/1/2017.
 */
public class Guardian {

    int trip_id;
    String name, imageUrl;

    public Guardian(){}

    public Guardian(int trip_id, String name, String imageUrl){
        this.trip_id = trip_id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Guardian(JSONObject response){

        try {
            response = response.getJSONObject("result");

            try {
                this.trip_id = response.getInt("trip_id");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Trip id is null");
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


        } catch (JSONException ex) {

            try {
                this.trip_id = response.getInt("trip_id");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Trip id is null");
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
        }
    }

    public int getTrip_id() {return trip_id;}

    public String getName() {return name;}

    public String getImageUrl() {return imageUrl;}
}
