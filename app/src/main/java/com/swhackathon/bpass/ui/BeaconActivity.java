package com.swhackathon.bpass.ui;

import android.Manifest;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.swhackathon.bpass.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BeaconActivity  extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG1 = "::MonitoringActivity::";
    protected static final String TAG2 = "::RangingActivity::";
    private BeaconManager beaconManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //이 클래스의 싱글 톤 인스턴스에 대한 접근 자입니다. 컨텍스트가 제공되어야하지만 비 활동 또는
        //비 서비스 클래스에서 사용해야하는 경우 Android 애플리케이션 클래스의 다른 싱글 톤
        //또는 서브 클래스에 연결할 수 있습니다.
        beaconManager = BeaconManager.getInstanceForApplication(this);
        //getBeaconParsers() = 활성 비콘 파서 목록을 가져옵니다.
        //거기에 새로운 비콘파서 형식을 만들어서 .add() 합니다
        //setBeaconLayout(String) = BLE 알림 내에서 0으로 색인화 된 오프셋을 바이트로 지정하는 문자열을 기반으로 비콘 필드 구문 분석 알고리즘을 정의합니다.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        //Android Activity또는 Service에 바인딩 합니다 BeaconService.
        beaconManager.bind(this);
        Log.d(TAG1, ":::::바인딩:::::");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Android Activity또는 Service에 바인딩 을 해제합니다 BeaconService.
        beaconManager.unbind(this);
        Log.d(TAG1, ":::::바인딩 해제:::::");
    }
    @Override
    //비콘 서비스가 실행 중이고 BeaconManager를 통해 명령을 수락 할 준비가되면 호출됩니다.
    public void onBeaconServiceConnect() {
        //모든 범위 알리미를 제거한다.
        beaconManager.removeAllRangeNotifiers();
        //BeaconService비콘 영역을 보거나 멈출 때 마다 호출해야하는 클래스를 지정합니다 .
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            //하나 이상의 비콘 Region이 표시 될 때 호출됩니다 .
            public void didEnterRegion(Region region) {
                Log.d(TAG1, ":::::최소하나의 비콘 발견하였음:::::");
            }
            @Override
            //비콘 Region이 보이지 않을 때 호출됩니다 .
            public void didExitRegion(Region region) {
                Log.d(TAG1, ":::::더이상 비콘을 찾을 수 없음:::::");
            }
            @Override
            //하나 이상의 비콘 Region이 표시 될 때 MonitorNotifier.INSIDE 상태 값으로 호출됩니다 .
            public void didDetermineStateForRegion(int state, Region region) {
                if(state==0){
                    Log.d(TAG1, ":::::비콘이 보이는 상태이다. state : "+state + ":::::");
                } else {
                    Log.d(TAG1, ":::::비콘이 보이지 않는 상태이다. state : "+state +":::::");
                }
            }
        });
        //범위한정 알리미를 추가한다
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            //눈에 보이는 비콘에 대한 mDistance(major 또는 minor와의 거리를 뜻하는)의 추정치를 제공하기 위해 초당 한 번 호출
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                List<Beacon> list = (List<Beacon>)beacons;
                if (beacons.size() > 0) {
                    Log.d(TAG2, ":::::The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.:::::");
                    Log.d(TAG2, ":::::This :: U U I D :: of beacon   :  "+ beacons.iterator().next().getId1().toString() + ":::::");
                    Log.d(TAG2, ":::::This ::M a j o r:: of beacon   :  "+ beacons.iterator().next().getId2().toString() + ":::::");
                    Log.d(TAG2, ":::::This ::M i n o r:: of beacon   :  "+ beacons.iterator().next().getId3().toString() + ":::::");
                }
            }
        });
        try {
            //알려주는 BeaconService전달 일치 비콘을 찾고 시작하는 Region개체를 지역에서 비콘을 볼 수있는 동안 추정 mDistance에있는 모든 초 업데이트를 제공합니다.
            beaconManager.startRangingBeaconsInRegion(new Region("C2:02:DD:00:13:DD", null, null, null));
        } catch (RemoteException e) {    }
    }
}