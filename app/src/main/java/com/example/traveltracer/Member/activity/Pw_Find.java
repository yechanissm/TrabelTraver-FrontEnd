package com.example.traveltracer.Member.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveltracer.Member.Data.FindIdData;
import com.example.traveltracer.Member.Data.FindPwData;
import com.example.traveltracer.Member.Response.CommonResponse;
import com.example.traveltracer.Member.Service.MemberService;
import com.example.traveltracer.R;
import com.example.traveltracer.global.config.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pw_Find extends AppCompatActivity {

    TextView back;
    EditText idView, nameView, emailView;
    Button pwfindBnt;

    private MemberService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pw_find);

        service = RetrofitConfig.getClient().create(MemberService.class);


        // back 누를 시 로그인 폼으로 이동
        back = findViewById(R.id.pwfindBack);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login_form.class);
            startActivity(intent);
        });

        // 아이디 입력 부분
        idView = findViewById(R.id.pwfindId);

        // 이름 입력 부분
        nameView = findViewById(R.id.pwfindName);

        // 이메일 입력 부분
        emailView = findViewById(R.id.pwfindMail);

        // 비밀번호 찾기 버튼
        pwfindBnt= findViewById(R.id.pwfindButton);


        // 비밀번호 찾기 버튼 클릭 시 다이얼 로그 활성화
        pwfindBnt = findViewById(R.id.pwfindButton);
        pwfindBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idView.setError(null);
                nameView.setError(null);
                emailView.setError(null);

                String userName = nameView.getText().toString();
                String userEmail = emailView.getText().toString();
                String userId = idView.getText().toString();

                boolean cancel =false;
                View focusView = null;

                if (userName.isEmpty()) {
                    nameView.setError("이름을 입력해주세요.");
                    focusView = nameView;
                    cancel = true;
                }
                if(userEmail.isEmpty()) {
                    emailView.setError("이메일을 입력해주세요.");
                    focusView = emailView;
                    cancel = true;
                }
                if(userId.isEmpty()) {
                    idView.setError("아이디를 입력해주세요.");
                    focusView = idView;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                }
                else{
                    showDialog(new FindPwData(userId, userName, userEmail));
                }
            }
        });
    }
    // 다이얼 로그 디자인
    public void showDialog(FindPwData findPwData){

        AlertDialog.Builder pwDialog = new AlertDialog.Builder(Pw_Find.this);

        service.findPw(findPwData).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse body = response.body();
                if(body.getMessage().equals("성공")) {
                    pwDialog.setTitle("아이디 찾기 결과");
                    pwDialog.setMessage("회원님의 이메일로 임시 비밀번호를 발급하였습니다.");
                    pwDialog.setIcon(R.drawable.ic_launcher_background);

                }
                else {
                    pwDialog.setTitle("아이디 찾기 결과");
                    pwDialog.setMessage("존재하지 않는 회원입니다");
                    pwDialog.setIcon(R.drawable.ic_launcher_background);
                }
                // 확인 버튼
                pwDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Pw_Find.this,"확인을 누르셨습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Pw_Find.this, Login_form.class);
                    }
                });
                pwDialog.show();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                String message = t.getMessage();
                Log.v("err", message);
                Toast.makeText(Pw_Find.this, message, Toast.LENGTH_SHORT).show();
                Log.e("아이디 찾기 에러 발생", t.getMessage());
            }
        });
    }
}