//package com.example.myapplication;
//
//
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.VideoView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class SplashActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_video);
//
//        VideoView videoView = findViewById(R.id.videoView);
//        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video);
//        videoView.setVideoURI(videoUri);
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                // Set the playback speed (1.0 for normal speed)
//                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(2f));
//                videoView.start();
//            }
//        });
//
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                // Video playback completed, navigate to MainActivity
//                navigateToMainActivity();
//            }
//        });
//    }
//
//    private void navigateToMainActivity() {
//        Intent intent = new Intent(this, dashboard.class);
//        startActivity(intent);
//        finish();  // Finish the splash activity to prevent going back to it when pressing the back button
//    }
//}


package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_video);

        VideoView videoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video);
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(2f));
                videoView.start();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // Check if the username is saved in SharedPreferences
                String username = getSavedUsername();
                if (username != null && !username.isEmpty()) {
                    // Username is saved, navigate to dashboard
                    navigateToDashboard();
                } else {
                    // No saved username, navigate to login page
                    navigateToLogin();
                }
            }
        });
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }

    private String getSavedUsername() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PREF_USERNAME, null);
    }
}
