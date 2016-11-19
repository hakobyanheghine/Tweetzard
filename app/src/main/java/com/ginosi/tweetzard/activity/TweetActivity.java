package com.ginosi.tweetzard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ginosi.tweetzard.R;
import com.ginosi.tweetzard.manager.ConnectivityManager;
import com.ginosi.tweetzard.util.ConnectivityChangeListener;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

/**
 * Created by heghine on 11/19/16.
 */

public class TweetActivity extends AppCompatActivity implements ConnectivityChangeListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        final EditText tweetEditText = (EditText) findViewById(R.id.activity_tweet_tweet_edit);
        findViewById(R.id.activity_tweet_tweet_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetText = tweetEditText.getText().toString();
                tweetEditText.setText("");
                if (tweetText.isEmpty()) {
                    // show toast
                } else {
                    TweetComposer.Builder builder = new TweetComposer.Builder(TweetActivity.this)
                            .text(tweetText);
                    builder.show();

//                    TwitterSession session = Twitter.getSessionManager().getActiveSession();
//                    Intent intent = new ComposerActivity.Builder(TweetActivity.this)
//                            .session(session)
//                            .createIntent();
//                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.activity_tweet_history_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTweetHistoryActivity();
            }
        });
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

        Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_tweet), message, Snackbar.LENGTH_LONG);
        snackbar.setDuration(duration);
        snackbar.show();
    }

    private void startTweetHistoryActivity() {
        TweetActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent tweetHistoryActivity = new Intent(TweetActivity.this, TweetHistoryActivity.class);
                startActivity(tweetHistoryActivity);
            }
        });
    }
}
