package com.ginosi.tweetzard.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by heghine on 11/19/16.
 */

public class PreferenceManager {

    private static PreferenceManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String NAME = "Tweetzard";

    public static final String PREFERENCE_USER_TWEETER_ID = "user_tweeter_id";
    public static final String PREFERENCE_USER_TWEETER_NAME = "user_tweeter_name";

    private PreferenceManager() {

    }

    public static PreferenceManager getInstance() {
        if (instance == null) {
            instance = new PreferenceManager();
        }
        return instance;
    }

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public long getUserTweeterId() {
        return sharedPreferences.getLong(PREFERENCE_USER_TWEETER_ID, 0);
    }

    public void setUserTweeterId(long value) {
        editor.putLong(PREFERENCE_USER_TWEETER_ID, value).commit();
    }

    public String getUserTweeterName() {
        return sharedPreferences.getString(PREFERENCE_USER_TWEETER_NAME, "");
    }

    public void setUserTweeterName(String value) {
        editor.putString(PREFERENCE_USER_TWEETER_NAME, value).commit();
    }
}
