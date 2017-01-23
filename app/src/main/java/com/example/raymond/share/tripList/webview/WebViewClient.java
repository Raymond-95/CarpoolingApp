package com.example.raymond.share.tripList.webview;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

/**
 * Created by Raymond on 21/1/2017.
 */
public class WebViewClient {

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(Uri.parse(url).getHost().endsWith("html5rocks.com")) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }

}