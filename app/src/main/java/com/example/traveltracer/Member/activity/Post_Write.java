package com.example.traveltracer.Member.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.example.traveltracer.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceTypes;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;


public class Post_Write extends AppCompatActivity {

    private static LatLng Input_LatLng = null;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "FinalTest";

    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_write);

        // 취소 버튼 클릭 시 뒤로 가기
        cancel = findViewById(R.id.postwritecancel_button);
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, Post_Main.class);
            startActivity(intent);
        });

        // 카테고리 콤보 박스(spinner) 부분
        Spinner spinner = (Spinner) findViewById(R.id.postwrite_category);
        // 어댑터 생성 및 array 레이아웃 가져오기
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.post_category, android.R.layout.simple_spinner_dropdown_item);
        // 스피너 어댑터 적용
        spinner.setAdapter(adapter);

/*-------------------- 주소 자동입력 부분 --------------------*/
        // 주소 검색 자동 완성 부분
        Places.initialize(getApplicationContext(), "AIzaSyBZDepCxR87WA_PB7yXr-CKFBjbK52TDhQ");
        PlacesClient placesClient = Places.createClient(this);

        // 자동 완성 Fragment 초기화
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

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
    }
}