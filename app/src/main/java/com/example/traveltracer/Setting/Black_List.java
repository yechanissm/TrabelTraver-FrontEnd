package com.example.traveltracer.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.traveltracer.R;

public class Black_List extends AppCompatActivity {

    TextView back;
    ListView balcklist;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.black_list);

        // back 버튼 클릭 시 환경 설정 화면으로 이동
        back = findViewById(R.id.black_list_back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, setting.class);
            startActivity(intent);
        });

        // 추가하기 버튼 클릭 시 화면 이동
        button = findViewById(R.id.black_list_button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, Create_Black_List.class);
            startActivity(intent);
        });
    }
}