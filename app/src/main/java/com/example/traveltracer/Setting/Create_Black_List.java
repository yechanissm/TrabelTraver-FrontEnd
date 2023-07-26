package com.example.traveltracer.Setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.traveltracer.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class Create_Black_List extends AppCompatActivity {

    private static final String TAG = "FinalTest";
    TextView Latitude, Hardness;
    Button cancel_btn, create_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_black_list);

        /*-------------------- 주소 자동입력 부분 --------------------*/
        // 주소 검색 자동 완성 부분
        Places.initialize(getApplicationContext(), "AIzaSyBZDepCxR87WA_PB7yXr-CKFBjbK52TDhQ");
        PlacesClient placesClient = Places.createClient(this);

        // 자동 완성 Fragment 초기화
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.address_search);

        // 국가 한국
        autocompleteFragment.setCountries("KR");
        // 선택한 장소 세부사항 필드
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // 자동 완성 된 주소 클릭 시 이벤트 처리
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }
            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // 취소 버튼 클릭 시 블랙리스트 화면으로 이동
        cancel_btn = findViewById(R.id.cancel_black_list_button);
        cancel_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, Black_List.class);
            startActivity(intent);
        });
    }
}