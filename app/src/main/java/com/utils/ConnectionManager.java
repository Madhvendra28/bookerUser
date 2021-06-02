package com.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bookkr.user.R;


/**
 * Created by ANKIT on 26-Sep-15.
 */
public class ConnectionManager {

    public static boolean isOnline(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = connectivityManager.getActiveNetworkInfo();
        if (net != null && net.isConnectedOrConnecting()) {
            return true;
        } else {
//            createDialog(activity);
            return false;
        }
    }

    public static void createDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.error_no_internet);
        builder.setPositiveButton("Try Again Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
