package com.swhackathon.bpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private int SPLASH_VIEW_TIME = 1000;
    private Handler delayHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishSplash();
            }
        }, SPLASH_VIEW_TIME);
    }

    private void finishSplash(){
        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivity(intent);
        finish();
    }
}