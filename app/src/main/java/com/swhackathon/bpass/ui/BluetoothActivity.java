package com.swhackathon.bpass.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.swhackathon.bpass.R;

public class BluetoothActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView bluetooth;
    private AnimationDrawable frameAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        toolbar = findViewById(R.id.toolbar);
        bluetooth = findViewById(R.id.iv_bluetooth);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_before);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bluetooth.setBackgroundResource(R.drawable.bluetooth_anim);
        frameAnimation = (AnimationDrawable) bluetooth.getBackground();

        frameAnimation.start();
    }
}