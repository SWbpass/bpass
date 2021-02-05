package com.swhackathon.bpass.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.Manifest;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.swhackathon.bpass.db.AppDatabase;
import com.swhackathon.bpass.R;
import com.swhackathon.bpass.db.Visit;
import com.swhackathon.bpass.db.VisitDao;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;
import java.util.List;

public class BeaconActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView bluetooth;
    private AnimationDrawable frameAnimation;
    private List<Visit> visitData;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String TAG = "sampleCreateBeacon";
    private BeaconTransmitter beaconTransmitter;
    private boolean isRunning = false;

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

        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "visit-db")
                .build();

        //db.visitDao().insert(new Visit("스타벅스 서울역동자동","1522-3232","10 : 25 : 45", "01 : 15 : 23", 126.9694083, 37.549469));
        //db.visitDao().insert(new Visit("이디야커피 용산청파","02-711-7385","11 : 42 : 01", "03 : 27 : 10", 126.9736404,  37.5513775));

        new InsertAsynTsak(db.visitDao())
                .execute(new Visit("스타벅스 서울역동자동","1522-3232", "서울특별시 용산구 동자동 한강대로 372","10 : 25 : 45", "01 : 15 : 23", 126.9694083, 37.549469));

        // 퍼미션 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    }
                });
                builder.show();
            }
        }

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRunning = !isRunning;
                if(isRunning) {
                    // 비콘 생성 후 시작. 실제 가장 필요한 소스
                    Beacon beacon = new Beacon.Builder()
                            .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")  // uuid for beacon
                            .setId2("1")  // major
                            .setId3("1")  // minor
                            .setManufacturer(0x0118)  // Radius Networks. 0x0118 : Change this for other beacon layouts // 0x004C : for iPhone
                            .setTxPower(-59)  // Power in dB
                            .setDataFields(Arrays.asList(new Long[]{0l}))  // Remove this for beacon layouts without d: fields
                            .build();

                    BeaconParser beaconParser = new BeaconParser()
                            .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
                    beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
                    beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
                        @Override
                        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                            super.onStartSuccess(settingsInEffect);
                            Log.e(TAG, "onStartSuccess: ");
                            frameAnimation.start();
                            isRunning = true;
                        }

                        @Override
                        public void onStartFailure(int errorCode) {
                            super.onStartFailure(errorCode);
                            Log.e(TAG, "onStartFailure: " + errorCode);
                            frameAnimation.stop();
                            isRunning = false;
                        }
                    });
                } else {
                    frameAnimation.stop();
                }
            }
        });
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

    // 퍼미션 요청후 callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult: ");

        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(isRunning) {
            beaconTransmitter.stopAdvertising();
            frameAnimation.stop();
        }
        super.onBackPressed();
    }
}