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
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.swhackathon.bpass.R;
import com.swhackathon.bpass.db.AppDatabase;
import com.swhackathon.bpass.db.Visit;
import com.swhackathon.bpass.db.VisitDao;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;
import java.util.List;

public class BluetoothActivity extends AppCompatActivity implements BeaconConsumer {

    private Toolbar toolbar;
    private ImageView bluetooth;
    private AnimationDrawable frameAnimation;
    private List<Visit> visitData;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String TAG = "sampleCreateBeacon";

    protected static final String TAG1 = "::MonitoringActivity::";
    protected static final String TAG2 = "::RangingActivity::";
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    private BeaconManager beaconManager;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

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
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Log.d(TAG, "onStartSuccess: ");
            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                Log.d(TAG, "onStartFailure: " + errorCode);
            }
        });
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

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    beaconManager.unbind(BluetoothActivity.this);
                    isRunning = false;
                    frameAnimation.stop();
                }
                else {
                    if (ContextCompat.checkSelfPermission(BluetoothActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    //이 클래스의 싱글 톤 인스턴스에 대한 접근 자입니다. 컨텍스트가 제공되어야하지만 비 활동 또는
                    //비 서비스 클래스에서 사용해야하는 경우 Android 애플리케이션 클래스의 다른 싱글 톤
                    //또는 서브 클래스에 연결할 수 있습니다.
                    beaconManager = BeaconManager.getInstanceForApplication(BluetoothActivity.this);
                    //getBeaconParsers() = 활성 비콘 파서 목록을 가져옵니다.
                    //거기에 새로운 비콘파서 형식을 만들어서 .add() 합니다
                    //setBeaconLayout(String) = BLE 알림 내에서 0으로 색인화 된 오프셋을 바이트로 지정하는 문자열을 기반으로 비콘 필드 구문 분석 알고리즘을 정의합니다.
                    beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
                    //Android Activity또는 Service에 바인딩 합니다 BeaconService.
                    beaconManager.bind(BluetoothActivity.this);

                    isRunning = true;
                    frameAnimation.start();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");

        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("", "coarse location permission granted");
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
}