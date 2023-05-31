package com.example.traveltracer.Member.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.traveltracer.R;

public class Id_Find extends AppCompatActivity {

    TextView back;
    EditText name, mail;
    Button idfindBtn, iddialogBnt;
    Dialog iddialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_find);

        // back 누를 시 로그인 폼으로 이동
        back = findViewById(R.id.idfindBack);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login_form.class);
            startActivity(intent);
        });

        // 이름 입력 부분
        name = findViewById(R.id.idfindName);

        // 이메일 입력 부분
        mail = findViewById(R.id.idfindMail);

        // 아이디 찾기 버튼
        idfindBtn = findViewById(R.id.idfindButton);

        // 다이얼 로그 초기화, 타이틀 제거, xml 연결
        iddialog = new Dialog(Id_Find.this);
        iddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        iddialog.setContentView(R.layout.id_find_dialog);

        // 아이디 찾기 버튼 클릭 시 다이얼 로그 활성화
        idfindBtn = findViewById(R.id.idfindButton);
        idfindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    // 다이얼 로그 디자인
    public void showDialog(){
        iddialog.show();
        // 여기에 아이디 값 및 경고 내용 넣기
        // *주의할 점: findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙여야 한다.

        // 확인 버튼
        iddialogBnt = iddialog.findViewById(R.id.idDialogButton);
        iddialogBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iddialog.dismiss(); // 확인 버튼 클릭 시 다이얼로그 종료
            }
        });
    }
}