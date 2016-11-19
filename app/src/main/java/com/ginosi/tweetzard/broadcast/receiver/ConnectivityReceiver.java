package com.ginosi.tweetzard.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ginosi.tweetzard.manager.ConnectivityManager;
import com.ginosi.tweetzard.util.Utils;

/**
 * Created by heghine on 11/19/16.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = Utils.isNetworkAvailable(context);
        Log.d("heghine", "ConnectivityReceiver: onReceive: isConnected = " + isConnected);
        ConnectivityManager.getInstance().notifySubscribers(isConnected);
    }

}
