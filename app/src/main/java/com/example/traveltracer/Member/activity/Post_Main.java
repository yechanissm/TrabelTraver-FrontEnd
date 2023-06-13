package com.example.traveltracer.Member.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.traveltracer.R;

public class Post_Main extends AppCompatActivity {

    TextView back;
    Spinner arrange, category;
    ListView postlist;
    Button createpost, droppost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_main);

        // back 클릭 시 메인화면으로 이동
        back = findViewById(R.id.postback);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
        });

        // 작성하기 버튼 클릭 시 작성 화면으로 이동
        createpost = findViewById(R.id.createpost_button);
        createpost.setOnClickListener(v -> {
            Intent intent = new Intent(this, Post_Write.class);
            startActivity(intent);
        });

        // 카테고리 선택 시 이벤트 구현(진행중)
    }
}