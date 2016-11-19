package com.ginosi.tweetzard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ginosi.tweetzard.activity.TweetActivity;
import com.ginosi.tweetzard.manager.ConnectivityManager;
import com.ginosi.tweetzard.manager.PreferenceManager;
import com.ginosi.tweetzard.util.ConnectivityChangeListener;
import com.ginosi.tweetzard.util.Utils;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements ConnectivityChangeListener {

    private static final String TWITTER_KEY = "gKG3bllWtjhYXuDYXiIVBiFza";
    private static final String TWITTER_SECRET = "Y3TJTvUtr0a9P8lgYRyylwXOWVKEDC1ecKfcOLGlGGoR3tLdPl";


    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);


        PreferenceManager.getInstance().init(this);

        boolean isConnected = Utils.isNetworkAvailable(this);
        if (!isConnected) {
            showNetworkMessage(isConnected);
        }

        loginButton = (TwitterLoginButton) findViewById(R.id.activity_main_login_btn);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("heghine", "TwitterCallback: success: result = " + result.data.toString());
                String username = result.data.getUserName();
                long userId = result.data.getUserId();

                PreferenceManager.getInstance().setUserTweeterName(username);
                PreferenceManager.getInstance().setUserTweeterId(userId);

                startTweetActivity();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("heghine", "TwitterCallback: failure: exception = " + exception.getMessage());
            }
        });

        if (!Utils.isLoginNecessary()) {
            loginButton.setVisibility(View.GONE);
            startTweetActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        ConnectivityManager.getInstance().subscribe(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        ConnectivityManager.getInstance().remove(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        showNetworkMessage(isConnected);
    }

    private void showNetworkMessage(boolean isConnected) {
        String message;
        int duration;
        if (isConnected) {
            message = getResources().getString(R.string.internet_connection);
            duration = Snackbar.LENGTH_SHORT;
        } else {
            message = getResources().getString(R.string.no_internet_connection);
            duration = Snackbar.LENGTH_INDEFINITE;
        }

        Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), message, Snackbar.LENGTH_LONG);
        snackbar.setDuration(duration);
        snackbar.show();
    }

    private void startTweetActivity() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent tweetActivity = new Intent(MainActivity.this, TweetActivity.class);
                startActivity(tweetActivity);
            }
        });
    }
}
