package com.swhackathon.bpass.ui;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.swhackathon.bpass.ListAdapter;
import com.swhackathon.bpass.PointAdapter;
import com.swhackathon.bpass.R;
import com.swhackathon.bpass.network.data.responsedata.VisitListData;

import java.util.ArrayList;

public class ListPersonActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar toolbar;
    private ListAdapter listAdapter;
    private ArrayList<VisitListData> visitListData;
    private RecyclerView rv_list;
    private LinearLayout map_view;
    private TextView tv_name, tv_email;
    private ImageButton btn_location;
    private SharedPreferences sharedPreferences;

    private NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private boolean isShowMap = false;
    private Marker marker;
    private InfoWindow infoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_person);

        toolbar = findViewById(R.id.toolbar);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        btn_location = findViewById(R.id.btn_location);
        rv_list = findViewById(R.id.rv_list);
        map_view = findViewById(R.id.map_view);
        isShowMap = false;
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_before);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedPreferences = this.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        tv_name.setText(sharedPreferences.getString("name", "해커톤"));
        tv_email.setText(sharedPreferences.getString("email", "B-pass@gmail.com"));

        // NaverMapAPI initialize
        NaverMapOptions options = new NaverMapOptions()
                /*.camera(new CameraPosition(new LatLng(35.1798159, 129.0750222), 8))*/
                .mapType(NaverMap.MapType.Terrain);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.fragment_map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(options);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowMap = !isShowMap;
                rv_list.setVisibility(isShowMap ? View.GONE : View.VISIBLE);
                map_view.setVisibility(isShowMap ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show();
                return;
            }

            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            Toast.makeText(this, "권한 승인됨", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setMapType(NaverMap.MapType.Basic);
        naverMap.setLocationSource(locationSource); // 현재 위치
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setScaleBarEnabled(true);
        uiSettings.setZoomControlEnabled(true);
        uiSettings.setLocationButtonEnabled(true);

        getReceivedData(); // FCM Service로 수신된 Notification에서 데이터를 가져옴
    }

    public void getReceivedData() {
        Bundle extras = getIntent().getExtras(); // FCM Service에서 Notification에 포함된 intent로 값을 넘김
        if (extras == null) { // 넘어온 값이 없을 경우
            return;
        }

        // 전달받은 Intent에서 데이터 추출
        final String storeName = extras.get("storeName").toString();
        final String storePhoneNumber = extras.get("storePhoneNumber").toString();
        final String address = extras.get("address").toString();
        final double latitude = Double.parseDouble(extras.get("latitude").toString());
        final double longitude = Double.parseDouble(extras.get("longitude").toString());

        Log.e("Map storeName >>", storeName);
        Log.e("Map storePhoneNumber >>", storePhoneNumber);
        Log.e("Map address >>", address);
        Log.e("Map latitude >>", String.valueOf(latitude));
        Log.e("Map longitude >>", String.valueOf(longitude));

        // 매장 위치로 카메라 이동
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(latitude, longitude))
                .animate(CameraAnimation.Fly);
        naverMap.moveCamera(cameraUpdate);

        // 지도에 마커 생성
        marker = new Marker();
        marker.setPosition(new LatLng(latitude, longitude));
        marker.setMap(this.naverMap);

        // 마커 클릭 시 커스텀 정보창 표시
        marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                ViewGroup rootView = (ViewGroup) findViewById(R.id.fragment_map);
                PointAdapter adapter = new PointAdapter(ListPersonActivity.this, rootView, storeName, storePhoneNumber, address, latitude, longitude);

                infoWindow = new InfoWindow();
                infoWindow.setAdapter(adapter);
                infoWindow.setZIndex(10); // 우선순위
                infoWindow.setAlpha(0.9f); // 투명도
                infoWindow.open(marker);
                return false;
            }
        });

        // 지도 두번 탭하면 정보창을 닫음
        naverMap.setOnMapDoubleTapListener(new NaverMap.OnMapDoubleTapListener() {
            @Override
            public boolean onMapDoubleTap(@NonNull PointF pointF, @NonNull LatLng latLng) {
                if(infoWindow != null && infoWindow.isVisible()) {
                    infoWindow.close();
                }
                return false;
            }
        });

    }
}