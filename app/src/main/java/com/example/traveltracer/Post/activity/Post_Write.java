package com.example.traveltracer.Post.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.traveltracer.Post.activity.MultiImageAdapter;
import com.example.traveltracer.Post.activity.Post_Main;
import com.example.traveltracer.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;


public class Post_Write extends AppCompatActivity {
    private static LatLng Input_LatLng = null;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "FinalTest";

    // 다중 사진 추가 부분
    private static final String TAG1 = "Post_Write";
    // 이미지 uri를 담을 ArratList 객체
    ArrayList<Uri> uriList = new ArrayList<>();
    // 이미지를 보여줄 리사이클러뷰와 적용시킬 어댑터
    RecyclerView recyclerView;
    MultiImageAdapter adapter;

    Button cancel, image_btn;

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

/* --------- 다중 사진 넣기 부분 ----------*/
        // 사진 추가 버튼 클릭 시 앨범으로 이동
        image_btn = findViewById(R.id.postwriteimg_button);
        image_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });
        recyclerView = findViewById(R.id.postwrite_image);
    }

    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){ // 아무 이미지도 선택 안했을 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.",
                    Toast.LENGTH_LONG).show();
        }
        else { // 이미지를 하나라도 선택한 경우
            if (data.getClipData() == null){
                Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);
                //어댑터 연결
                adapter = new MultiImageAdapter(uriList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                // 수평으로 적용
                recyclerView.setLayoutManager(new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL, true));
            }
            else { // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));
                // 사진 10장까지 가능 추후 수정 가능
                if(clipData.getItemCount() > 10){ // 선택한 이미지가 11장 이상일 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택" +
                            "가능합니다. ", Toast.LENGTH_LONG).show();
                }
                else { // 선택한 이미지가 10장 이하인 경우
                    Log.e(TAG, "multiple choice");
                    // 이미지 갯수 확인
                    for (int i = 0; i < clipData.getItemCount(); i++){
                        // 선택한 이미지들의 uri를 가져온다.
                        Uri imageUri = clipData.getItemAt(i).getUri();

                        try { // uri를 list에 담는다.
                            uriList.add(imageUri);
                        } catch (Exception e){
                            Log.e(TAG, "File select error", e);
                        }
                    }

                    adapter = new MultiImageAdapter(uriList,
                            getApplicationContext());
                    // 리사이클러뷰에 어댑터 세팅
                    recyclerView.setAdapter(adapter);
                    // 리사이클러뷰에 수평 스크롤 적용
                    recyclerView.setLayoutManager(new LinearLayoutManager(this,
                            LinearLayoutManager.HORIZONTAL, true));
                }
            }
        }
    }
}