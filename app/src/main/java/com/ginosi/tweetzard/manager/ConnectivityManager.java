package com.ginosi.tweetzard.manager;

import com.ginosi.tweetzard.util.ConnectivityChangeListener;

import java.util.ArrayList;

/**
 * Created by heghine on 11/19/16.
 */

public class ConnectivityManager {
    private static ConnectivityManager instance;

    private ConnectivityManager() {

    }

    public static ConnectivityManager getInstance() {
        if (instance == null) {
            instance = new ConnectivityManager();
        }

        return instance;
    }

    public ArrayList<ConnectivityChangeListener> connectivityChangeListeners = new ArrayList<>();

    public void subscribe(ConnectivityChangeListener listener) {
        connectivityChangeListeners.add(listener);
    }

    public void remove(ConnectivityChangeListener listener) {
        connectivityChangeListeners.remove(listener);
    }

    public void notifySubscribers(boolean isConnected) {
        for (int i = 0; i < connectivityChangeListeners.size(); i++) {
            connectivityChangeListeners.get(i).onConnectivityChanged(isConnected);
        }
    }
}
