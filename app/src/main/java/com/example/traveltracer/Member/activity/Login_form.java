package com.example.traveltracer.Member.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.traveltracer.MainActivity;
import com.example.traveltracer.Member.Data.LoginData;
import com.example.traveltracer.Member.Response.CommonResponse;
import com.example.traveltracer.Member.Service.MemberService;
import com.example.traveltracer.R;
import com.example.traveltracer.global.config.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_form extends AppCompatActivity {

    TextView sign;
    Button login;
    EditText userIdView,userPasswordView;
    private MemberService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_form);

        userIdView = findViewById(R.id.editID);
        userPasswordView = findViewById(R.id.editPassword);

        service = RetrofitConfig.getClient().create(MemberService.class);

        //회원가입 버튼
        sign = findViewById(R.id.signin);
        sign = findViewById(R.id.signin);

        //회원가입 버튼 클릭시, 회원가입 페이지로 이동
        sign.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        });

        //로그인 버튼
        login = findViewById(R.id.loginbutton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userIdView.setError(null);
                userPasswordView.setError(null);


                String userId = userIdView.getText().toString();
                String userPassword = userPasswordView.getText().toString();

                View focusView = null;
                boolean not_valid =false;
                if (userId.isEmpty()) {
                    userIdView.setError("아이디를 입력해주세요.");
                    focusView = userIdView;
                    not_valid = true;
                }
                if (userPassword.isEmpty()) {
                    userPasswordView.setError("비밀번호를 입력해주세요.");
                    focusView = userPasswordView;
                    not_valid = true;
                }
                if (not_valid) {
                    focusView.requestFocus();
                }
                //만약 빈칸이 없이 제대로 동작하는 거라면.
                else{
                    service.login(new LoginData(userId, userPassword)).enqueue(new Callback<CommonResponse>() {

                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            Log.v("실행", "로그인버");
                            Log.d("실행", userId);
                            Log.d("실행", userPassword);

                            CommonResponse commonResponse = response.body();
                            if(commonResponse.getMessage().equals("실패")) {
                                Toast.makeText(Login_form.this, "아이디 혹은 비밀번호가 일치하지 않습니다." , Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intent = new Intent(Login_form.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            String message = t.getMessage();
                            Toast.makeText(Login_form.this,message , Toast.LENGTH_SHORT).show();
                            Log.e("회원가입 에러 발생", t.getMessage());
                        }
                    });
                }
            }
        });
    }
}
