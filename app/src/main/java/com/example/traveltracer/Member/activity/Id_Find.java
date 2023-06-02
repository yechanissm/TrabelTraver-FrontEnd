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

import com.example.traveltracer.MainActivity;
import com.example.traveltracer.Member.Data.FindIdData;
import com.example.traveltracer.Member.Response.CommonResponse;
import com.example.traveltracer.Member.Service.MemberService;
import com.example.traveltracer.R;
import com.example.traveltracer.global.config.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Id_Find extends AppCompatActivity {

    TextView back;
    EditText name, email;
    Button idfindBtn;

    private MemberService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_find);

        service = RetrofitConfig.getClient().create(MemberService.class);


        // back 누를 시 로그인 폼으로 이동
        back = findViewById(R.id.idfindBack);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login_form.class);
            startActivity(intent);
        });

        // 이름 입력 부분
        name = findViewById(R.id.idfindName);

        // 이메일 입력 부분
        email = findViewById(R.id.idfindMail);

        // 아이디 찾기 버튼
        idfindBtn = findViewById(R.id.idfindButton);


        // 아이디 찾기 버튼 클릭 시 다이얼 로그 활성화
        idfindBtn = findViewById(R.id.idfindButton);
        idfindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setError(null);
                email.setError(null);

                String userName = name.getText().toString();
                String userEmail = email.getText().toString();

                boolean cancel =false;
                View focusView = null;

                if (userName.isEmpty()) {
                    name.setError("이름을 입력해주세요.");
                    focusView = name;
                    cancel = true;
                }
                if(userEmail.isEmpty()) {
                    email.setError("이메일을 입력해주세요.");
                    focusView = email;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                }
                else{
                    showDialog(new FindIdData(userName, userEmail));

                }
            }
        });
    }

    // 다이얼 로그 디자인
    public void showDialog(FindIdData findIdData){

        service.findId(findIdData).enqueue(new Callback<CommonResponse>() {
            AlertDialog.Builder iddialog = new AlertDialog.Builder(Id_Find.this);

            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse body = response.body();
                if(body.getMessage().equals("실패")) {
                    iddialog.setTitle("아이디 찾기 결과");
                    iddialog.setMessage("존재하지 않는 회원입니다");
                    iddialog.setIcon(R.drawable.ic_launcher_background);

                }
                else {
                    iddialog.setTitle("아이디 찾기 결과");
                    iddialog.setMessage("회원님의 아이디는 " + body.getMessage() + "입니다.");
                    iddialog.setIcon(R.drawable.ic_launcher_background);
                }
                // 확인 버튼
                iddialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Id_Find.this,"확인을 누르셨습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Id_Find.this, Login_form.class);
                        startActivity(intent);
                    }
                });
                iddialog.show();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                String message = t.getMessage();
                System.out.println(message);
                Toast.makeText(Id_Find.this, message, Toast.LENGTH_SHORT).show();
                Log.e("아이디 찾기 에러 발생", t.getMessage());
            }
        });

    }
}