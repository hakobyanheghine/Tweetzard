package com.ginosi.tweetzard.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ginosi.tweetzard.R;
import com.ginosi.tweetzard.manager.ConnectivityManager;
import com.ginosi.tweetzard.manager.PreferenceManager;
import com.ginosi.tweetzard.util.ConnectivityChangeListener;
import com.ginosi.tweetzard.util.Utils;
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

        if (!Utils.isNetworkAvailable(this)) {
            Utils.showNetworkMessage(false, TweetHistoryActivity.this, findViewById(R.id.activity_tweet_history));
        }

        ConnectivityManager.getInstance().subscribe(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        ConnectivityManager.getInstance().remove(this);
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        Utils.showNetworkMessage(isConnected, TweetHistoryActivity.this, findViewById(R.id.activity_tweet_history));
    }

}
