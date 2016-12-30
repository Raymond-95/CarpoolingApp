package com.example.raymond.share;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Raymond on 26/12/2016.
 */
public class GetUserId {

    private static String object;
    private static String result;

    public void getID(Context context, String objectName){

        object = objectName;

        GlobalUsage prefix = new GlobalUsage();
        String url = prefix.getPrefix() + "showUser.php";

        RequestQueue requestQueue;

        requestQueue = Volley.newRequestQueue(context);

        // Creating the JsonObjectRequest class called obreq, passing required parameters:
        //GET is used to fetch data from the server, JsonURL is the URL to be fetched from.
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //JSONObject obj = response.getJSONObject("data");

                            JSONArray users = response.getJSONArray("users");
                            for (int i=0; i< users.length(); i++){
                                JSONObject user = users.getJSONObject(i);

                                object = user.getString("name");
                                result = object;
                            }
                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        // Adds the JSON object request "obreq" to the request queue
        requestQueue.add(jsonObjReq);
    }

    public String getObject(Context context, String objectName){

        getID(context, objectName);
        return result;
    }

}
