package com.example.raymond.share.jsonparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShareJSON {

    private static final String TAG = "share.shared.json";

    int mCode;
    String mBody, mSubject;

    JSONObject mResult;
    JSONArray mResults;

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public JSONObject getResult() {
        return mResult;
    }

    public JSONArray getResults() {
        return mResults;
    }

    public ShareJSON(){
    }

    public ShareJSON get(JSONObject jsonObject) {

        try{
            Log.d(TAG, jsonObject.toString());

            try {

                JSONObject messageObject = jsonObject.getJSONObject("meta");
                mCode = messageObject.getJSONObject("status").getInt("code");
                messageObject = messageObject.getJSONObject("msg");
                mSubject = messageObject.getString("subj");
                mBody = messageObject.getString("body");

                try {
                    mResult = jsonObject.getJSONObject("result");
                } catch (JSONException e1) {
                    mResults = jsonObject.getJSONArray("result");
                }

            } catch (Exception e) {
                Log.e(TAG, "Error converting JSON", e);
            }

        }catch (NullPointerException ex){
            Log.d(TAG, "JSON response return NULL");
            mBody = "";
            mCode = 0;
            mSubject = "";
        }

        return this;
    }

}
