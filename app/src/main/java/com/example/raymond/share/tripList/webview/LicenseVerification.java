package com.example.raymond.share.tripList.webview;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.raymond.share.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

public class LicenseVerification extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView mWebView;
    private static String url;
    private static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_verification);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("License Verification");

        mWebView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        url = "http://www.jpj.gov.my/web/guest/tarikh-luput-lesen-memandu";

        new Title().execute();

    }

    private class Title extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Connection.Response res = null;
            try {
                res = Jsoup
                        .connect(url)
                        .data("catid", "1")
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //This will get you cookies
            Map<String, String> cookies = res.cookies();

            try {
                Document doc = Jsoup.connect(url).cookies(cookies).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }


    @Override
    public void onBackPressed(){
        if (mWebView.canGoBack()){

            mWebView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }

}
