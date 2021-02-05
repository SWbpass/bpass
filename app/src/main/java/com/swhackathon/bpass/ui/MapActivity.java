package com.swhackathon.bpass.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.swhackathon.bpass.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // NaverMapAPI initialize
        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(35.1798159, 129.0750222), 8))
                .mapType(NaverMap.MapType.Terrain);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(options);
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                Toast.makeText(MapActivity.this, "권한 거부됨", Toast.LENGTH_SHORT).show();
                return;
            }

            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            Toast.makeText(MapActivity.this, "권한 승인됨", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setMapType(NaverMap.MapType.Basic);
        naverMap.setLocationSource(locationSource); // 현재 위치

        /*
        TODO: 현재 위치를 초기 좌표로 설정하도록 수정 필요
         */
        // 지정 좌표로 카메라 이동
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(36.763695, 127.281796)).animate(CameraAnimation.Fly);
        naverMap.moveCamera(cameraUpdate);

        getReceivedData(); // FCM Service로 수신된 Notification에서 데이터를 가져옴
    }

    public void getReceivedData() {
        Bundle extras = getIntent().getExtras(); // FCM Service에서 Notification에 포함된 intent로 값을 넘김
        if (extras == null) { // 넘어온 값이 없을 경우
            /*
            TODO: 전달받은 값이 없는 경우 처리
             */
            return;
        }

        /*
        TODO: 마커 수정 필요
        storeName, storePhoneNumber, address 추가로 넘겨받아서 마커에 정보 표기
         */
        // 전달받은 Intent에서 데이터 추출
//        double latitude = Double.parseDouble((String)extras.get("latitude"));
//        double longitude = Double.parseDouble((String)extras.get("longitude"));

        double latitude = Double.parseDouble(extras.get("latitude").toString());
        double longitude = Double.parseDouble(extras.get("longitude").toString());

        Log.e("MapActivity latitude >>", String.valueOf(latitude));
        Log.e("MapActivity longitude>>", String.valueOf(longitude));

        // 지도에 마커 생성
        Marker marker = new Marker();
        marker.setPosition(new LatLng(latitude, longitude));
        marker.setMap(this.naverMap);
        // 마커 위치로 카메라 이동
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(latitude, longitude))
                .animate(CameraAnimation.Fly);
        naverMap.moveCamera(cameraUpdate);

    }
}