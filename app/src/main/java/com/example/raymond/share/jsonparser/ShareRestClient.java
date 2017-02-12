package com.example.raymond.share.jsonparser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.raymond.share.Login;
import com.example.raymond.share.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by karhong on 10/4/13.
 */

public class ShareRestClient {

    private static final String TAG = "Share.RestClient";
	//private static final String API_URL = "https://api-v4.getShare.com";
    public static final String WEB_URL = "http://192.168.1.214:8000";
    public static final String API_URL = WEB_URL + "/api";

    public static final int RC_AUTH = 9722;

    public static final String POST = "POST";
    public static final String GET = "GET";

    private static AsyncHttpClient mClient;
    private PersistentCookieStore mCookieStore;

    private Context mContext;
    private String mHttpMethod;
    private String mEndPoint;

    private ShareApi.DialogResponseHandler mDialogResponseHandler;
    private ShareApi.CustomJsonResponseHandler mCustomJsonResponseHandler;
    private ProgressDialog mProgressDialog;

    private boolean mKeepProgressDialog = false;
    private boolean mShowMessageDialog = true;
    private boolean mFinishActivity = false;
    private boolean mExitWhen401 = true;

    private ShareJSON mShareJSON;

    private Command mSuccessCommand, mFailureCommand;

    RequestParams mParams = new RequestParams();

    public ShareRestClient(Context context) {
        this.mContext = context;

//        if(mCookieStore == null)
//            mCookieStore = new PersistentCookieStore(context);

        if (mClient == null) {
            mClient = new AsyncHttpClient();
            mCookieStore = new PersistentCookieStore(context);
            setCookieStore();
        }

        Log.d("cookies", String.valueOf(mCookieStore));
    }

    public ShareRestClient syncHttpClient(){

        if (mClient == null) {
            mClient = new SyncHttpClient();
            mClient.setCookieStore(mCookieStore);
        }
        return this;
    }

    public void setMethod(String httpMethod){
        mHttpMethod = httpMethod;
    }

    public void setEndPoint(String endPoint){
        mEndPoint = endPoint;
    }

    public void addParam(String name, DecimalFormat value) {
        mParams.put(name, value);
    }

    public void addParam(String name, String value) {
        mParams.put(name, value);
    }

    public void addParam(String name, double value) {
        mParams.put(name, String.valueOf(value));
    }

    public void addParam(String name, int value) { mParams.put(name, String.valueOf(value));}

    public void addParam(String name, InputStream value) {
        mParams.put(name, value);
    }

    public void addParam(String name, Date value) { mParams.put(name, value);}

    public void addParam(String name, Time value) { mParams.put(name, value);}

    public void addParam(String name, File value) {
        try {
            mParams.put(name, value);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addParam(String name, Bitmap value) {
        mParams.put(name, value);
    }

    private boolean checkInternetConnection(){

        if (Share.Helpers.checkInternet(mContext))
            return true;

        Toast.makeText(mContext, "Please connect to the internet", Toast.LENGTH_LONG).show();

        return false;
    }

    public void get() {
        Log.d(TAG, mParams.toString());
        if (checkInternetConnection()) {
            Log.e("URL", getAbsoluteUrl(mEndPoint));
            mClient.get(getAbsoluteUrl(mEndPoint), mParams, mJsonHttpResponseHandler);
        }
    }

    public void post() {
        Log.d(TAG, mParams.toString());
        if (checkInternetConnection()) {
            Log.e("URL", getAbsoluteUrl(mEndPoint));
            mClient.post(getAbsoluteUrl(mEndPoint), mParams, mJsonHttpResponseHandler);
        }
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return API_URL + relativeUrl;
    }

    public void setCookieStore() {
        mClient.setCookieStore(mCookieStore);
    }

    public PersistentCookieStore getCookieStore() {
        return mCookieStore;
    }

    public List<Cookie> getCookies() {
        return mCookieStore.getCookies();
    }

    public void clearCookies() {
        mCookieStore.clear();
    }

    private JsonHttpResponseHandler mJsonHttpResponseHandler = new JsonHttpResponseHandler(){

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response){

            mShareJSON = new ShareJSON().get(response);

            if(mProgressDialog != null && !mKeepProgressDialog) mProgressDialog.dismiss();

            if(mDialogResponseHandler != null)
                mDialogResponseHandler.onSuccess(response, mShareJSON);

            if(mCustomJsonResponseHandler != null)
                mCustomJsonResponseHandler.onSuccess(response, mShareJSON);

            if(mShowMessageDialog) showDialogIfNeeded();

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response){

            mShareJSON = new ShareJSON().get(response);

            if(response == null)
                Toast.makeText(mContext, "Internet connection error", Toast.LENGTH_SHORT).show();

            if(mProgressDialog != null) mProgressDialog.dismiss();

            if(mDialogResponseHandler != null)
                mDialogResponseHandler.onFailure(throwable, response, mShareJSON);

            if(mCustomJsonResponseHandler != null)
                mCustomJsonResponseHandler.onFailure(throwable, response, mShareJSON);

            if(mShowMessageDialog) showDialogIfNeeded();
        }
    };

    public void request(ShareApi.DialogResponseHandler responseHandler){

        //setup progress dialog
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            //mProgressDialog.setTitle(mContext.getResources().getString(R.string.please_wait));
            mProgressDialog.setMessage(mContext.getResources().getString(R.string.loading));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }

        mDialogResponseHandler = responseHandler;
        callService();

    }

    public void request(ShareApi.CustomJsonResponseHandler responseHandler){

        mCustomJsonResponseHandler = responseHandler;
        callService();

    }

    private void callService(){
        Log.d(TAG, getAbsoluteUrl(mEndPoint));
        if(mHttpMethod.equals(POST)){
            post();
        }else if(mHttpMethod.equals(GET)){
            get();
        }
    }

    public void keepProgressDialog(){
        mKeepProgressDialog = true;
    }

    public ProgressDialog getProgressDialog(){
        return mProgressDialog;
    }
    public void setProgressDialog(ProgressDialog progressDialog){
        mProgressDialog = progressDialog;
    }

    public void showMessageDialog(boolean showMessageDialog){
        mShowMessageDialog = showMessageDialog;
    }

    public void setFinishActivity(boolean finishActivity){
        mFinishActivity = finishActivity;
    }

    public void setExitWhen401(boolean exitWhen401) {
        mExitWhen401 = exitWhen401;
    }

    private void showDialogIfNeeded(){

        if(!mShareJSON.getBody().equals("")) {
            final AlertDialog.Builder alertDialog;
            alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setCancelable(false);
            alertDialog.setTitle(mShareJSON.getSubject());
            alertDialog.setMessage(mShareJSON.getBody());

            if(mExitWhen401 && mShareJSON.getCode() == 401)
                alertDialog.setCancelable(false);

            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    if(mShareJSON.getCode() == 401)
                        handle401Error();

                    if(mSuccessCommand != null && mShareJSON.getCode() == 200)
                        mSuccessCommand.execute();

                    else if(mFailureCommand != null && mShareJSON.getCode() != 200)
                        mFailureCommand.execute();

                    else if(mFinishActivity) ((Activity) mContext).finish();

                }
            });
            alertDialog.show();
        }
    }


    public ShareRestClient onSuccessDialogClose(Command command) {
        mSuccessCommand = command;
        return this;
    }

    public ShareRestClient onFailureDialogClose(Command command) {
        mFailureCommand = command;
        return this;
    }

    public interface Command {
        void execute();
    }

    private void handle401Error(){

        if(mExitWhen401) {
            new ShareRestClient(mContext).clearCookies();
            Intent intent = new Intent(mContext, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Share.INTENT_LOGIN, true);
            ((Activity) mContext).setResult(Activity.RESULT_OK);
            ((Activity) mContext).startActivityForResult(intent, RC_AUTH);
        }
    }
}