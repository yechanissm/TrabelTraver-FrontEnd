package com.example.traveltracer.Member.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.traveltracer.MainActivity;
import com.example.traveltracer.Member.map.LocationUtils;
import com.example.traveltracer.Member.map.MarkerUtils;
import com.example.traveltracer.Member.map.locationData;
import com.example.traveltracer.R;
import com.google.android.material.navigation.NavigationView;

//지도 부분 import
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class Main extends AppCompatActivity implements OnMapReadyCallback {

    Dialog logoutdialog; // 커스텀 다이얼 로그

    // 지도 연동
    public GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private LocationUtils locationUtils;
    private MarkerUtils markerUtils;

    private com.example.traveltracer.Member.map.locationData locationData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //지도를 화면에 표시하는 부분
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = getLocationRequest();
        locationCallback = getLocationCallback();
        locationData = new locationData();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.lab1_map);
        mapFragment.getMapAsync(this);

        this.InitializeLayout();

        // 지도 버튼 활성화
        setButton1();
        setButton2();

        locationUtils = new LocationUtils(this);
        markerUtils = new MarkerUtils(map);

    }
    //위도 경도 표시 버튼
    private void setButton1() {
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
    }
    // 현재위치 표시 버튼
    private void setButton2() {
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLocationPermission()) {
                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        nowCurrentLocation(location);
                                    }
                                }
                            });
                }
            }
        });
    }

    // 지도 위치 요청
    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    // 지도 호출 부분 구글 맵을 객체로 받아 실행
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (checkLocationPermission()) {
            locationUtils.requestLocationUpdates(locationRequest, locationCallback);
        }
    }
    // 현재 위치 정보 가져 오는 부분 (1번 버튼 눌렀을 때)
    private void getCurrentLocation() {
        if (checkLocationPermission()) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                double longitude = location.getLongitude();
                                double latitude = location.getLatitude();
                                locationData.setnewLongitude(longitude);
                                locationData.setnewLatitude(latitude);
                                Toast.makeText(Main.this, "현재 위치: 위도 " + locationData.getnewLatitude() + ", 경도 " + locationData.getnewLongitude(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    private void nowCurrentLocation(Location location) {
        locationUtils.CurrentLocationMove(location);
    }

    private boolean checkLocationPermission() {
        return locationUtils.checkLocationPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationUtils.requestLocationUpdates(locationRequest, locationCallback);
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationUtils.stopLocationUpdates(locationCallback);
    }

    private LocationCallback getLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        locationUtils.displayCurrentLocation(location);
                    }
                }
            }
        };
    }

    public void InitializeLayout() {
        //toolBar를 통해 App Bar 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //App Bar의 좌측 영영에 Drawer를 Open 하기 위한 Incon 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.navi_menu);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.app_name,
                R.string.app_name
        );
        drawer.addDrawerListener(actionBarDrawerToggle);


        // navigation 객체에 nav_view의 참조 반환
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // navigation 객체에 이벤트 리스너 달기
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.menuItem1) {
                    Toast.makeText(getApplicationContext(), "친구 목록 이동", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.menuItem2) {
                    // 클릭 시 게시물 창으로 이동
                    findViewById(R.id.menuItem2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Main.this,Post_Main.class);
                            startActivity(intent);
                        }
                    });
                } else if (itemId == R.id.menuItem3) {
                    Toast.makeText(getApplicationContext(), "환경설정 이동", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.menuItem4) {
                    // 다이얼 로그 초기화, 타이틀 제거, xml 연결
                    logoutdialog = new Dialog(Main.this);
                    logoutdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    logoutdialog.setContentView(R.layout.logout_dialog);

                    // 로그아웃 버튼 클릭 시 다이얼 로그 활성화
                    findViewById(R.id.menuItem4).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog();
                        }
                    });
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    // 뒤로 가기 시 사이드 바 닫기
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // 다이얼 로그 부분
    public void showDialog(){
        logoutdialog.show();

        // 네 버튼 - 클릭 시 로그아웃 후 로그인 폼으로
        Button yesBtn = logoutdialog.findViewById(R.id.logoutYesBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            // 로그아웃 후 로그인 창으로 이동
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Login_form.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        // 아니요 버튼
        Button noBtn = logoutdialog.findViewById(R.id.logoutNoBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutdialog.dismiss(); // 다이얼로그 닫기
            }
        });

    }



}

