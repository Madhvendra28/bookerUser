package com.utils;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import androidx.annotation.Keep;


/**
 * Created by ANKIT on 26-Sep-15.
 */
public class GetServerData {
    private static RequestQueue requestQueue;
    private static final String TAG = "GetServerData";

    @Keep
    private static void createRequestQueue(Activity activity) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
        }
    }

    @Keep
    public static void addRequestToQueue(Activity activity, Request request) {
        if (requestQueue == null)
            createRequestQueue(activity);
        requestQueue.add(request);
    }

}
