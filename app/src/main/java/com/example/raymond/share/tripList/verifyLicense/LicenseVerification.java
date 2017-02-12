package com.example.raymond.share.tripList.verifyLicense;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.raymond.share.R;

public class LicenseVerification extends AppCompatActivity {

    private Toolbar toolbar;
    private static WebView mWebView;
    private static WebView tempView;
    private static String url;
    private static ProgressDialog progressDialog;
    private static int index = 207;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_verification);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("License Verification");

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mWebView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        url = "http://www.jpj.gov.my/web/guest/tarikh-luput-lesen-memandu";

        mWebView.addJavascriptInterface(new JavascriptAccessor(), "JavascriptAccessor");
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished (WebView webView, String url)
            {
                tempView = webView;
                Button submit = (Button) findViewById(R.id.submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        tempView.evaluateJavascript(
                                "(function() { return ('<html>'+document.getElementById('resultAjax').innerHTML+'</html>'); })();",
                                new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String html) {
                                        Log.d("HTML", html);
                                        finalResult(html);
                                    }
                                });
                    }
                });
            }
        });

        mWebView.loadUrl(url);
    }

    private class JavascriptAccessor {
        @SuppressWarnings("unused")
        public void processHTML(String html)
        {

        }
    }

    public void finalResult(String html){

        if (html.length() < 30){

        }
        else{

            String[] licenseInfo = new String [7];

            //get name
            licenseInfo[0] = getInfo(html, 0);

            //get IC number
            licenseInfo[1] = getInfo(html, 124);

            //get Category ID
            licenseInfo[2] = getInfo(html, 120);

            //get License Type
            licenseInfo[3] = getInfo(html, 1078);

            //get License Class
            licenseInfo[4] = getInfo(html, 204);

            //get License issue date
            licenseInfo[5] = getInfo(html, 319);

            //get License end date
            licenseInfo[6] = getInfo(html, 114);

            Intent intent = new Intent(getApplicationContext(), LicenseInfo.class);
            intent.putExtra("license_info", licenseInfo);
            startActivity(intent);
            finish();
        }
    }

    public String getInfo(String html, int i){

        String stop;

        StringBuilder object = new StringBuilder("");
        stop = "";
        index = index + i;
        while (!object.toString().contains("\\")){

            stop = html.substring(index, index+1);
            object.append(stop);
            index++;
        }

        String value = object.toString().substring(0, object.toString().length()-1);
        Log.d("Object", value);

        return value;
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

    @Override
    protected void onStop() {
        super.onStop();
        url = null;
        progressDialog = null;
    }
}
