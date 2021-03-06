package com.swhackathon.bpass.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.swhackathon.bpass.R;

public class ChoiceActivity extends AppCompatActivity {

    private Button img_store;
    private Button img_person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        img_store = findViewById(R.id.iv_store);
        img_person = findViewById(R.id.iv_person);

        img_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("Who", 0);
                startActivity(intent);
                finish();
            }
        });
        img_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("Who", 1);
                startActivity(intent);
                finish();
            }
        });
    }
}