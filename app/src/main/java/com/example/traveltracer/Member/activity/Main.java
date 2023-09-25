package com.example.traveltracer.Member.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.traveltracer.Location.CheckPoint.CheckpointManager;
import com.example.traveltracer.Post.activity.Post_Main;
import com.example.traveltracer.R;
import com.example.traveltracer.Setting.setting;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationView;

public class Main extends AppCompatActivity implements OnMapReadyCallback {

    Dialog logoutdialog; // 커스텀 다이얼 로그

    // 지도 연동
    private GoogleMap map;

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    //위치 값 받을 떄 사용되는 객체
    private LocationCallback locationCallback;
    private com.example.traveltracer.Location.CheckPoint.CheckpointManager checkpointManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.lab1_map);
        mapFragment.getMapAsync(this);

        this.InitializeLayout();
    }
    /*

    -----------------------------------------------------지도 관련 부분 --------------------------------------------------------------------

    */


    // 지도 호출 부분 구글 맵을 객체로 받아 실행
    // 지도 호출 부분 구글 맵을 객체로 받아 실행
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // 권한이 부여되었으므로 My Location 레이어를 활성화합니다
            enableMyLocation();
            // CheckpointManager 인스턴스 생성
            checkpointManager = new CheckpointManager(this, map);
            // setupCheckpoint() 메서드 호출
            checkpointManager.setupCheckpoint();
        } else {
            // 권한이 부여되지 않았으므로 요청합니다
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    //my location 레이어 활성화하는 부분
    private void enableMyLocation() {
        try {
            map.setMyLocationEnabled(true); //내 위치로 이동 부분
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //워치 권한 동의 여부 확인 부분
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                enableMyLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    protected void onDestroy() {
        super.onDestroy();
    }



/*
 ------------------------------------------------------------------ 레이아웃 및 구성 부분-----------------------------------------------------------------------------
*/
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
                            Intent intent = new Intent(Main.this, Post_Main.class);
                            startActivity(intent);
                        }
                    });
                } else if (itemId == R.id.menuItem3) {
                    findViewById(R.id.menuItem3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Main.this, setting.class);
                            startActivity(intent); // 환경설정 버튼 클릭 시 창 이동
                        }
                    });}
                else if (itemId == R.id.menuItem4) {
                    // 다이얼 로그 초기화, 타이틀 제거,ㅂㅈ xml 연결
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

