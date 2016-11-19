package com.ginosi.tweetzard.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.Preference;

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
}
