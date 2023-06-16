package com.example.traveltracer.Post.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.traveltracer.Member.activity.Main;
import com.example.traveltracer.R;

public class Post_Main extends AppCompatActivity {

    TextView back;
    ListView postlist, listView;
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

        // 정렬 콤보 박스(spinner) 부분
        Spinner spinner = (Spinner) findViewById(R.id.arrange_spinner);
        // 어댑터 생성 및 array 레이아웃 가져오기
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.post_arrange, android.R.layout.simple_spinner_dropdown_item);
        // 스피너 어댑터 적용
        spinner.setAdapter(adapter);

        // 카테고리 콤보 박스(spinner) 부분
        Spinner spinner1 = (Spinner) findViewById(R.id.category_spinner);
        // 어댑터 생성 및 array 레이아웃 가져오기
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.post_category, android.R.layout.simple_spinner_dropdown_item);
        // 스피너 어댑터 적용
        spinner1.setAdapter(adapter1);

    }
}