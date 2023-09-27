package com.example.traveltracer.Setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.traveltracer.Location.service.LocationService;
import com.example.traveltracer.Member.Response.CommonResponse;
import com.example.traveltracer.R;
import com.example.traveltracer.Setting.Data.BlackListData;
import com.example.traveltracer.Setting.Service.BlackListService;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Create_Black_List extends AppCompatActivity {

    private static final String TAG = "FinalTest";
    private EditText latitude_et, longitude_et, title_et, address_et;
    private Button cancel_btn, conversion_btn, create_btn;
    private BlackListService service;

    private int locationId = 1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_black_list);

        // 레이아웃 정의 (위도, 경도, 제목, 주소)
        latitude_et = findViewById(R.id.latitude_editText);
        longitude_et = findViewById(R.id.longitude_editText);
        title_et = findViewById(R.id.title_editText);
        address_et = findViewById(R.id.address_input);
        conversion_btn = findViewById(R.id.conversion_button);

        // 버튼 클릭 시 이벤트 설정
        // 주소를 가져와 지오 코딩 실행
        conversion_btn.setOnClickListener(v -> {
            String address = address_et.getText().toString();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault()); // 지오코딩

            try { // 예외처리 및 매개 변수 결과는 1개만 나타난다.
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address location = addresses.get(0); // 목록에서 첫 번째 주소를 검색
                    double latitude = location.getLatitude(); // 주소에서 위도 가져옴
                    double longitude = location.getLongitude(); // 주소에서 경도 가져옴

                    latitude_et.setText(String.valueOf(latitude)); // 가져온 값으로 업데이트
                    longitude_et.setText(String.valueOf(longitude));
                } else {
                    Log.e(TAG, "No address found for: " + address);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Geocoding failed: " + e.getMessage());
            }
        });

        // 추가 버튼 클릭 시 이벤트 처리
        create_btn = findViewById(R.id.create_blacklist_button);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlackListData blackListData = new BlackListData();
                blackListData.setLatitude(Double.parseDouble(latitude_et.getText().toString()));
                blackListData.setLongitude(Double.parseDouble(longitude_et.getText().toString()));
                blackListData.setTitle(title_et.getText().toString());
                blackListData.setAddress(address_et.getText().toString());

                service.BlackListSave(blackListData).enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.isSuccessful()) {
                            CommonResponse result = response.body();
                            if (result != null) {
                                if (result.getCode() == 200) {
                                    Toast.makeText(context, "저장완료", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "서버 에러: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "서버 응답이 null입니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "서버 응답 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        // Handle network error
                        Toast.makeText(context, "네트워크 에러: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        // 취소 버튼 클릭 시 이벤트 처리
        cancel_btn = findViewById(R.id.cancel_black_list_button);
        cancel_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, Black_List.class);
            startActivity(intent);
        });
    }
}
