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

import com.example.traveltracer.R;
import com.google.android.material.navigation.NavigationView;

public class Main extends AppCompatActivity {

    Dialog logoutdialog; // 커스텀 다이얼 로그
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.InitializeLayout();
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
                    Toast.makeText(getApplicationContext(), "게시물 목록 이동", Toast.LENGTH_SHORT).show();
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

