package com.swhackathon.bpass.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.swhackathon.bpass.ItemDecoration;
import com.swhackathon.bpass.ListAdapter;
import com.swhackathon.bpass.R;
import com.swhackathon.bpass.network.data.responsedata.VisitListData;

import java.util.ArrayList;

public class ListStoreActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListAdapter listAdapter;
    private ArrayList<VisitListData> visitListData;
    private RecyclerView rv_list;
    private TextView tv_name, tv_email;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_store);

        toolbar = findViewById(R.id.toolbar);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        rv_list = findViewById(R.id.rv_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_before);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedPreferences = this.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        tv_name.setText(sharedPreferences.getString("name", "해커톤"));
        tv_email.setText(sharedPreferences.getString("email", "B-pass@gmail.com"));

        listAdapter = new ListAdapter();
        rv_list.setAdapter(listAdapter);
        rv_list.addItemDecoration(new ItemDecoration(this,15));

        //showList();
    }

    public void showList(){
        visitListData.clear();
        listAdapter.mData = visitListData;
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
