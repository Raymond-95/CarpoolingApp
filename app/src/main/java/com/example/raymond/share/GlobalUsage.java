package com.example.raymond.share;

import android.app.Application;

/**
 * Created by Raymond on 26/12/2016.
 */
public class GlobalUsage extends Application {

    public String getPrefix(){

        String prefix = "http://192.168.0.2/Share/";

        return prefix;
    }


}
