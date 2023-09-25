package com.example.traveltracer.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.traveltracer.Member.activity.Main;
import com.example.traveltracer.R;

public class setting extends AppCompatActivity {

    TextView back, Blacklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        // back 클릭 시 메인화면으로 이동
        back = findViewById(R.id.setting_back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
        });

        // 블랙리스트 클릭 시 블랙리스트 화면으로 이동
        Blacklist = findViewById(R.id.setting_blacklist);
        Blacklist.setOnClickListener(v -> {
            Intent intent = new Intent(this, Black_List.class);
            startActivity(intent);
        });


    }
}