package com.ginosi.tweetzard.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.ginosi.tweetzard.R;
import com.ginosi.tweetzard.manager.ConnectivityManager;
import com.ginosi.tweetzard.manager.PreferenceManager;
import com.ginosi.tweetzard.util.ConnectivityChangeListener;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

/**
 * Created by heghine on 11/19/16.
 */

public class TweetHistoryActivity extends ListActivity implements ConnectivityChangeListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_history);

        UserTimeline timeline = new UserTimeline.Builder()
                .screenName(PreferenceManager.getInstance().getUserTweeterName())
                .build();


        TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(timeline)
                .build();

        setListAdapter(adapter);
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

        Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_tweet_history), message, Snackbar.LENGTH_LONG);
        snackbar.setDuration(duration);
        snackbar.show();
    }
}
