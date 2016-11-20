package com.ginosi.tweetzard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

        loginButton = (TwitterLoginButton) findViewById(R.id.activity_main_login_btn);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                String username = result.data.getUserName();
                long userId = result.data.getUserId();

                PreferenceManager.getInstance().setUserTweeterName(username);
                PreferenceManager.getInstance().setUserTweeterId(userId);

                startTweetActivity();
            }

            @Override
            public void failure(TwitterException exception) {
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

        if (!Utils.isNetworkAvailable(this)) {
            Utils.showNetworkMessage(false, MainActivity.this, findViewById(R.id.activity_main));
        }

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
        Utils.showNetworkMessage(isConnected, MainActivity.this, findViewById(R.id.activity_main));
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
