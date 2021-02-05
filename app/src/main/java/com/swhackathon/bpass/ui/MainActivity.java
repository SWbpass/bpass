package com.swhackathon.bpass.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.swhackathon.bpass.R;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView iv_profile;
    private TextView tv_name, tv_email;
    private ImageButton btn_camera, btn_list_store, btn_bluetooth, btn_list_person;
    private LinearLayout btn_store, btn_person;
    private int who;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        iv_profile = findViewById(R.id.iv_profile);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        btn_camera = findViewById(R.id.btn_camera);
        btn_list_store = findViewById(R.id.btn_list_store);
        btn_bluetooth = findViewById(R.id.btn_bluetooth);
        btn_list_person = findViewById(R.id.btn_list_person);
        btn_store = findViewById(R.id.btn_store);
        btn_person = findViewById(R.id.btn_person);

        sharedPreferences = this.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        who = getIntent().getIntExtra("Who",0);
        tv_name.setText(sharedPreferences.getString("name", "해커톤"));
        tv_email.setText(sharedPreferences.getString("email", "B-pass@gmail.com"));

        if(who == 0){
            btn_store.setVisibility(View.VISIBLE);
            btn_person.setVisibility(View.INVISIBLE);
            iv_profile.setImageResource(R.drawable.icon_store);
        }
        else{
            btn_store.setVisibility(View.INVISIBLE);
            btn_person.setVisibility(View.VISIBLE);
            iv_profile.setImageResource(R.drawable.icon_person);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_list_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListStoreActivity.class);
                startActivity(intent);
            }
        });

        btn_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
                startActivity(intent);
            }
        });

        btn_list_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListStoreActivity.class);
                startActivity(intent);
            }
        });

    }
}