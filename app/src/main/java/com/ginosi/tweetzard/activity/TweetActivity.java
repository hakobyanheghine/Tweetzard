package com.ginosi.tweetzard.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ginosi.tweetzard.R;
import com.ginosi.tweetzard.manager.ConnectivityManager;
import com.ginosi.tweetzard.util.ConnectivityChangeListener;
import com.ginosi.tweetzard.util.Utils;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import gun0912.tedbottompicker.TedBottomPicker;

/**
 * Created by heghine on 11/19/16.
 */

public class TweetActivity extends AppCompatActivity implements ConnectivityChangeListener {

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        final EditText tweetEditText = (EditText) findViewById(R.id.activity_tweet_tweet_edit);
        findViewById(R.id.activity_tweet_tweet_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetText = tweetEditText.getText().toString();

                if (tweetText.isEmpty() && imageUri == null) {
                    Toast.makeText(TweetActivity.this, R.string.empty_tweet, Toast.LENGTH_LONG).show();
                } else if (tweetText.isEmpty() && imageUri != null) {
                    TweetComposer.Builder builder = new TweetComposer.Builder(TweetActivity.this)
                            .image(imageUri);
                    builder.show();
                } else if (!tweetText.isEmpty() && imageUri == null) {
                    TweetComposer.Builder builder = new TweetComposer.Builder(TweetActivity.this)
                            .text(tweetText);
                    builder.show();
                } else {
                    TweetComposer.Builder builder = new TweetComposer.Builder(TweetActivity.this)
                            .text(tweetText)
                            .image(imageUri);
                    builder.show();
                }

                // resetting data
                tweetEditText.setText("");
                imageUri = null;
                TweetActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.activity_tweet_done_img).setVisibility(View.GONE);
                    }
                });
            }
        });

        findViewById(R.id.activity_tweet_history_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTweetHistoryActivity();
            }
        });

        findViewById(R.id.activity_tweet_photo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(TweetActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TweetActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    startImagePicker();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!Utils.isNetworkAvailable(this)) {
            Utils.showNetworkMessage(false, TweetActivity.this, findViewById(R.id.activity_tweet));
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
    }

    private void startImagePicker() {
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(TweetActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        imageUri = uri;
                        TweetActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                findViewById(R.id.activity_tweet_done_img).setVisibility(View.VISIBLE);
                            }
                        });
                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        Utils.showNetworkMessage(isConnected, TweetActivity.this, findViewById(R.id.activity_tweet));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startImagePicker();
                } else {
                    Toast.makeText(this, R.string.grant_this_perm, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
