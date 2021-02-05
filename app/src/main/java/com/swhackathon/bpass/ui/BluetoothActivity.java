package com.swhackathon.bpass.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.swhackathon.bpass.db.AppDatabase;
import com.swhackathon.bpass.R;
import com.swhackathon.bpass.db.Visit;
import com.swhackathon.bpass.db.VisitDao;

import java.util.List;

public class BluetoothActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView bluetooth;
    private AnimationDrawable frameAnimation;
    private List<Visit> visitData;

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

        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "visit-db")
                .build();

        //db.visitDao().insert(new Visit("스타벅스 서울역동자동","1522-3232","10 : 25 : 45", "01 : 15 : 23", 126.9694083, 37.549469));
        //db.visitDao().insert(new Visit("이디야커피 용산청파","02-711-7385","11 : 42 : 01", "03 : 27 : 10", 126.9736404,  37.5513775));

        new InsertAsynTsak(db.visitDao())
                .execute(new Visit("스타벅스 서울역동자동","1522-3232", "서울특별시 용산구 동자동 한강대로 372","10 : 25 : 45", "01 : 15 : 23", 126.9694083, 37.549469));

    }

    private static class InsertAsynTsak extends AsyncTask<Visit, Void, Void>{

        private VisitDao visitDao;

        public InsertAsynTsak(VisitDao visitDao){
            this.visitDao = visitDao;
        }

        @Override
        protected Void doInBackground(Visit... visits) {
            visitDao.insert(visits[0]);
            visitDao.insert(new Visit("이디야커피 용산청파","02-711-7385", "서울특별시 용산구 청파동 청파로71길 10","11 : 42 : 01", "03 : 27 : 10", 126.9736404,  37.5513775));
            return null;
        }
    }
}