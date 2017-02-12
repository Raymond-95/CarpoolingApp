package com.example.raymond.share.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by jiaminglow on 4/19/16.
 */

@Parcel
public class TripRequest {

    int id, user_id, trip_id;

    String name, imageUrl, status, source, destination, type;

    public TripRequest(){}

    public TripRequest(int id, int user_id, int trip_id, String name, String imageUrl, String status, String source, String destination, String type){
        this.id = id;
        this.user_id = user_id;
        this.trip_id = trip_id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.status = status;
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public TripRequest(JSONObject response) {

        try {
            response = response.getJSONObject("result");

            try {
                this.id = response.getInt("id");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Id is null");
            }

            try {
                this.user_id = response.getInt("requested_by");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "User id is null");
            }

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
                Log.d(getClass().getName(), "Image Url is null");
            }

            try {
                this.status = response.getString("status");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Status is null");
            }

            try {
                this.source = response.getString("source");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Source is null");
            }

            try {
                this.destination = response.getString("destination");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Destination is null");
            }

            try {
                this.type = response.getString("type");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Type is null");
            }

        } catch (JSONException ex) {

            try {
                this.id = response.getInt("id");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Id is null");
            }

            try {
                this.user_id = response.getInt("requested_by");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "User id is null");
            }

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
                Log.d(getClass().getName(), "Image Url is null");
            }

            try {
                this.status = response.getString("status");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Status is null");
            }

            try {
                this.source = response.getString("source");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Source is null");
            }

            try {
                this.destination = response.getString("destination");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Destination is null");
            }

            try {
                this.type = response.getString("type");
            } catch (JSONException e) {
                Log.d(getClass().getName(), "Type is null");
            }

        }
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return user_id;
    }

    public int getTripId() {
        return trip_id;
    }

    public String getUserName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {return destination;}
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getType() {return type;}
}
