package com.ginosi.tweetzard.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.Preference;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.ginosi.tweetzard.R;
import com.ginosi.tweetzard.manager.PreferenceManager;

/**
 * Created by heghine on 11/19/16.
 */

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isLoginNecessary() {
        return PreferenceManager.getInstance().getUserTweeterId() == 0 && PreferenceManager.getInstance().getUserTweeterName().isEmpty();
    }

    public static void showNetworkMessage(boolean isConnected, Activity activity, View viewToAttach) {
        String message;
        int duration;
        if (isConnected) {
            message = activity.getResources().getString(R.string.internet_connection);
            duration = Snackbar.LENGTH_SHORT;
        } else {
            message = activity.getResources().getString(R.string.no_internet_connection);
            duration = Snackbar.LENGTH_INDEFINITE;
        }

        Snackbar snackbar = Snackbar.make(viewToAttach, message, Snackbar.LENGTH_LONG);
        snackbar.setDuration(duration);
        snackbar.show();
    }
}
