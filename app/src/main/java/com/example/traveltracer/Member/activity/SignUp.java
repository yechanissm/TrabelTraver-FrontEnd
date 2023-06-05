package com.example.traveltracer.Member.activity;

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

import com.example.traveltracer.Member.Data.SignUpdata;
import com.example.traveltracer.Member.Response.CommonResponse;
import com.example.traveltracer.Member.Service.MemberService;
import com.example.traveltracer.R;
import com.example.traveltracer.global.config.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    TextView back;

    EditText userNameView,userIdView,userPasswordView,userPassword2View,ageView,userEmailView;
    Button idcheck, pwcheck, submit;
    private MemberService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("실행", "실행됨");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login_form.class);
            startActivity(intent);
        });
        //기입 항목
        userIdView = findViewById(R.id.signID);
        userNameView = findViewById(R.id.signName);
        userPasswordView = findViewById(R.id.signPW);
        userPassword2View = findViewById(R.id.signPW2);
        ageView = findViewById(R.id.signAge);
        userEmailView = findViewById(R.id.signEmail);
        service = RetrofitConfig.getClient().create(MemberService.class);

        //아이디 중복확인 버튼
        idcheck = findViewById(R.id.idcheckbutton);
        idcheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String userId = userIdView.getText().toString();
                checkID(userId);
            }
        });

        //비밀번호 확인 버튼
        pwcheck = findViewById(R.id.pwcheckbutton);
        pwcheck.setOnClickListener(v -> {
            if (userPasswordView.getText().toString().equals(userPassword2View.getText().toString())) {
                pwcheck.setText("일치");
            } else {
                Toast.makeText(SignUp.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
            }
        });

        //회원가입 완료 버튼
        submit = findViewById(R.id.signupbutton);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                userIdView.setError(null);
                userPasswordView.setError(null);
                userPassword2View.setError(null);
                userNameView.setError(null);
                ageView.setError(null);
                Log.v("실행", "버튼누른거확인됨");
                attemptJoin();
            }
        });
    }
        private void attemptJoin() {
            String userId = userIdView.getText().toString();
            String userPassword = userPasswordView.getText().toString();
            String userName = userNameView.getText().toString();
            String userEmail = userEmailView.getText().toString();
            String age= ageView.getText().toString();

            boolean cancel =false;
            View focusView = null;

            // 패스워드의 유효성 검사
            if (userPassword.isEmpty()) {
                userPasswordView.setError("비밀번호를 입력해주세요.");
                focusView = userPasswordView;
                cancel = true;
            }
            // 아이디를 유효성 검사
            if (userId.isEmpty()) {
                userIdView.setError("아이디를 입력해주세요.");
                focusView = userIdView;
                cancel = true;
            }

            // 이름의 유효성 검사
            if (userName.isEmpty()) {
                userNameView.setError("이름을 입력해주세요.");
                focusView = userNameView;
                cancel = true;
            }
            if (userName.isEmpty()) {
                userNameView.setError("이름을 입력해주세요.");
                focusView = userNameView;
                cancel = true;
            }

            if (userEmail.isEmpty()) {
                userEmailView.setError("이메일을 입력해주세요.");
                focusView = userEmailView;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            } else {
                startJoin(new SignUpdata(userId, userPassword, userName,userEmail, Integer.parseInt(age)));
            }
        }

    private void startJoin(SignUpdata signUpdata) {
        service.userSignUp(signUpdata).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse result = response.body();
                Toast.makeText(SignUp.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("여기까지 됨");
                if(result.getCode()==200) {
                    Toast.makeText(SignUp.this, "회원가입을 축하합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, Login_form.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                String message = t.getMessage();
                System.out.println(message);
                Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
            }
        });
    }

    private void checkID(String userId) {
        userIdView.setError(null);

        View idFocusView = null;
        boolean check_id =false;
        if (userId.isEmpty()) {
            userIdView.setError("아이디를 입력해주세요.");
            idFocusView = userIdView;
            check_id = true;
        }
        if (check_id) {
            idFocusView.requestFocus();
        } else {
            service.checkIdExist(userId).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonResponse checkIdResponse = response.body();
                    String result = checkIdResponse.getMessage();
                    if(result.equals("성공")){
                        Toast.makeText(SignUp.this,"사용 가능한 아이디입니다." , Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(SignUp.this, "이미 존재하는 회원입니다." , Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t){
                    String message = t.getMessage();
                    Toast.makeText(SignUp.this,message , Toast.LENGTH_SHORT).show();
                    Log.e("회원가입 에러 발생", t.getMessage());
                }

            });
        }

    }

}
