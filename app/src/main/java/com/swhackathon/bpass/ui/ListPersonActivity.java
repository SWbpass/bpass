package com.swhackathon.bpass.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.swhackathon.bpass.db.AppDatabase;
import com.swhackathon.bpass.ItemDecoration;
import com.swhackathon.bpass.ListPersonAdapter;
import com.swhackathon.bpass.R;
import com.swhackathon.bpass.db.Visit;

import java.util.List;

public class ListPersonActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListPersonAdapter listPersonAdapter;
    private List<Visit> visitData;
    private List<Visit> myData;
    private RecyclerView rv_list;
    private TextView tv_name, tv_email;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_person);

        toolbar = findViewById(R.id.toolbar);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        rv_list = findViewById(R.id.rv_list_person);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_before);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedPreferences = this.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        tv_name.setText(sharedPreferences.getString("name", "해커톤"));
        tv_email.setText(sharedPreferences.getString("email", "B-pass@gmail.com"));

        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "visit-db")
                .allowMainThreadQueries()
                .build();



        Log.d("디비", db.visitDao().getAll().toString());
        listPersonAdapter = new ListPersonAdapter();
        rv_list.setAdapter(listPersonAdapter);

        visitData = db.visitDao().getAll();
        listPersonAdapter.mData = visitData;
        rv_list.addItemDecoration(new ItemDecoration(this,15));
        listPersonAdapter.notifyDataSetChanged();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}