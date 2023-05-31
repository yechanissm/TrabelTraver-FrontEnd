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

public class Pw_Find extends AppCompatActivity {

    TextView back;
    EditText id, name, mail;
    Button pwfindBnt, pwdialogBnt;
    Dialog pwdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pw_find);

        // back 누를 시 로그인 폼으로 이동
        back = findViewById(R.id.pwfindBack);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login_form.class);
            startActivity(intent);
        });

        // 아이디 입력 부분
        id = findViewById(R.id.pwfindId);

        // 이름 입력 부분
        name = findViewById(R.id.pwfindName);

        // 이메일 입력 부분
        mail = findViewById(R.id.pwfindMail);

        // 비밀번호 찾기 버튼
        pwfindBnt= findViewById(R.id.pwfindButton);

        // 다이얼 로그 초기화, 타이틀 제거, xml 연결
        pwdialog = new Dialog(Pw_Find.this);
        pwdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pwdialog.setContentView(R.layout.pw_find_diaolg);

        // 비밀번호 찾기 버튼 클릭 시 다이얼 로그 활성화
        pwfindBnt = findViewById(R.id.pwfindButton);
        pwfindBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }
    // 다이얼 로그 디자인
    public void showDialog(){
        pwdialog.show();
        // 여기에 비밀 번호 이메일 발송 내용, 경고 내용 넣기
        // *주의할 점: findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙여야 한다.

        // 확인 버튼
        pwdialogBnt = pwdialog.findViewById(R.id.pwDialogButton);
        pwdialogBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdialog.dismiss(); // 확인 버튼 클릭 시 다이얼로그 종료
            }
        });
    }
}