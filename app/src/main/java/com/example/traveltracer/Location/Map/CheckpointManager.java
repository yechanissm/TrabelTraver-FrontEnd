package com.example.traveltracer.Location.Map;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.traveltracer.Location.Data.CheckPointData;
import com.example.traveltracer.Location.service.LocationService;
import com.example.traveltracer.Member.Response.CommonResponse;
import com.example.traveltracer.Member.activity.SignUp;
import com.example.traveltracer.global.config.RetrofitConfig;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckpointManager extends AppCompatActivity {

    private Context context;
    private LocationHelper locationHelper;
    private Location previousLocation;
    private List<LatLng> checkpoints;
    private Marker checkpointMarker;
    private GoogleMap map;

    private boolean isFirstLocationUpdate = true;
    private LocationCallback locationCallback;
    private static final long MINIMUM_UPDATE_INTERVAL = 1 * 60 * 1000; // 20 minutes
    private static final float MINIMUM_DISTANCE_THRESHOLD = 50; // 50 meters
    private static final long STAY_DURATION = 1 * 60 * 1000; // 20 minutes
    private HashMap<Location, Long> markerCreationTimes = new HashMap<>(); // 마커가 생성된 시간을 저장하는 맵

    private Handler handler;
    private Runnable timerRunnable;

    private LocationService service;

    private int locationId =1;

    private long currentTime;


    //CheckpointManger(객체 생성자)
    public CheckpointManager(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
        this.locationHelper = new LocationHelper(context);
        this.checkpoints = new ArrayList<>();
        this.handler = new Handler();
        this.service = RetrofitConfig.getLocationService();

        // 맵 객체가 null이 아닌 경우에만 마커를 추가하도록 초기화 시점을 변경합니다.
        if (this.map != null) {
            this.map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    setupCheckpoint();
                }
            });
        }
    }
    //현재 위치를 가져와 위치 업데이트 확인 하기 위한 타이머 시작
    public void setupCheckpoint() {
        //locationHelper.getCurrentLocation() : 현재 위치를 비동기적으로 가져오기 위해 사용하는 메서드
        //LocationCallback() : 현재 위치를 가져와 호출되는 콜백
        locationHelper.getCurrentLocation(new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) { //현재위치 정보를 처리
                if (locationResult != null) {  //위치 정보가 있으면 현재 위치 저장
                    Location currentLocation = locationResult.getLastLocation();
                    if (currentLocation != null) {  //현재 위치 값이 있으면 동작
                        updateCheckpoint(currentLocation); //경유지 업데이트
                        startTimer(); // 위치 업데이트 확인을 위한 타이머 시작(일정
                    }                 //일정 시간마다 현재 위치 확인, 일정 기준 충족 시 마커 생성
                }
            }
        });
    }

    /*
    *현재 위치와 이전 위치를 비교해 일정 거리, 일정 시간이 경과한 경우에만 경유지 없데이트
    *해당 위치에 마커 생성 여부 판단
    */
    // 경유지 업데이트 및 위치 업데이트가 된후 마커 생성 여부 판별하는 메소드
    private void updateCheckpoint(Location currentLocation) {
        if (previousLocation == null) {  //이전 정보가 없으면 새로운 위치 정보 저장
            previousLocation = currentLocation;  //현재 위치를 이전 위치로 설정
            // 최초 위치 정보
            saveNewCheckpoint(currentLocation);
            markerCreationTimes.put(currentLocation, System.currentTimeMillis()); //
        } else {  // 이전 위치 정보가 있는 경우
            float distance = currentLocation.distanceTo(previousLocation);  // 현재 위치와 이전 위치 사이 거리 차이 계산
            long timeDifference = currentLocation.getTime() - previousLocation.getTime();  // 현재 위치와 이전 위치 사이 시간 차이 계산

            //50미터 이상이거나 20분 이상 일 경우 경유지 업데이트
            if(distance >= MINIMUM_DISTANCE_THRESHOLD || timeDifference >= MINIMUM_UPDATE_INTERVAL) {
                saveNewCheckpoint(currentLocation);  //경유지 업데이트
                markerCreationTimes.put(currentLocation, System.currentTimeMillis()); // 마커 생성 시간 맵에 저장
            }
        }

        previousLocation = currentLocation;  //현재 위치를 이전 위치로 설정
        createMarkerIfLocationStays(currentLocation); // 위치 업데이트 후에 마커 생성 여부 확인
    }

    //새로운 경우지를 저장하는 역할
    private void saveNewCheckpoint(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());//location객체에 위도 경도 정보 가져와 latLng객체 생성
        checkpoints.add(latLng); //경유지들 위도 경도 정보 저장하는 리스트
    }

    private void startTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                // 현재 위치를 가져옴
                locationHelper.getCurrentLocation(new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult != null) {
                            Location currentLocation = locationResult.getLastLocation();
                            if (currentLocation != null) {
                                // 현재 위치에서 20분 동안 머무른 경우에만 마커 생성
                                createMarkerIfLocationStays(currentLocation);
                            }
                        }
                    }
                });

                // 다음 타이머 실행을 위해 타이머 재등록
                handler.postDelayed(this, STAY_DURATION);
            }
        };
        // 최초 실행을 위해 타이머 시작
        handler.postDelayed(timerRunnable, STAY_DURATION);
    }
    private void createMarkerIfLocationStays(Location location) {
        currentTime = System.currentTimeMillis();
        markerCreationTimes.put(location, currentTime); // 현재 위치의 마커 생성 시간을 맵에 저장

        List<Location> locationsToRemove = new ArrayList<>();

        // 이전 위치들 검사하여 머무른 위치에 마커가 생성되었는지 확인
        for (Map.Entry<Location, Long> entry : markerCreationTimes.entrySet()) {
            Location previousLocation = entry.getKey();
            long previousMarkerCreationTime = entry.getValue();

            // 이전 위치에서 현재 위치로의 시간 차이 계산
            long timeDifference = currentTime - previousMarkerCreationTime;

            if (previousLocation.distanceTo(location) < MINIMUM_DISTANCE_THRESHOLD && timeDifference >= STAY_DURATION) {
                // 이전 위치에서 현재 위치로의 거리가 MINIMUM_DISTANCE_THRESHOLD 이하이고,
                // 일정 시간(STAY_DURATION) 동안 머무른 경우에 마커 생성을 예약
                locationsToRemove.add(previousLocation);
                if (map != null) {
                    // 마커 생성 코드
                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(previousLocation.getLatitude(), previousLocation.getLongitude()))
                            //.title("이름을 뭘로 할까요?") <= 이름 생성 조건 작성
                            ;
                    map.addMarker(markerOptions);
                    if(previousLocation != null) {
                        String savepoint = "savePoint" + locationId;
                        savePoint(new CheckPointData(locationId, savepoint, previousLocation.getLongitude(), previousLocation.getLatitude(), currentTime));
                        locationId++;
                    }
                }

            }
        }

        // 예약된 마커 생성 실행
        for (Location locationToRemove : locationsToRemove) {
            markerCreationTimes.remove(locationToRemove);
        }
    }

    private void savePoint(CheckPointData CheckPointData){
        if (context == null) {
            Log.e("savePoint", "Context is null, cannot show Toast.");
            return; // context가 null이면 Toast를 생성하지 않고 함수 종료
        }

        service.CheckPointSave(CheckPointData).enqueue(new Callback<CommonResponse>(){
            /*
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse result = response.body();
                //Toast.makeText(CheckpointManager.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("위치 저장 완료?");
                if(result.getCode()==200){
                    Toast.makeText(CheckpointManager.this, "저장완료", Toast.LENGTH_SHORT).show();
                    doRepeatedTask();
                }
                else{
                    Toast.makeText(getApplicationContext(), "위치 저장 실패", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                String message = t.getMessage();
                System.out.println(message);
                Toast.makeText(CheckpointManager.this, message, Toast.LENGTH_SHORT).show();
                Log.e("위치 저장 에러 발생", t.getMessage());
            }*/
            // onResponse 메서드
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    CommonResponse result = response.body();
                    if (result != null) {
                        if (result.getCode() == 200) {
                            Toast.makeText(context, "저장완료", Toast.LENGTH_SHORT).show();
                            doRepeatedTask();
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

            // onFailure 메서드
            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                String message = t.getMessage();
                Log.e("위치 저장 에러 발생", message);
                Toast.makeText(context, "네트워크 에러: " + message, Toast.LENGTH_SHORT).show();

                // 실패한 경우에 대한 추가 처리를 수행할 수 있습니다.
                // 예를 들어, 사용자에게 다시 시도하도록 안내하는 등의 작업을 수행할 수 있습니다.
            }

        });
    }
    private void doRepeatedTask() {
        final Handler handler = new Handler();
        final int delay = 100000; // 작업을 반복할 주기 (예: 10분ㅂㅈ마다)

        handler.postDelayed(new Runnable() {
            public void run() {
                // 원하는 작업을 수행하는 코드를 여기에 추가
                // 이 코드는 delay 시간마다 실행됩니다.

                // 예: 위치 저장과 관련된 작업을 반복적으로 수행하려면 여기에 추가
                savePoint(new CheckPointData(locationId, "savePoint" + locationId, previousLocation.getLongitude(), previousLocation.getLatitude(), currentTime));
                locationId++;

                // 다음 실행을 위해 handler.postDelayed()를 호출하여 작업을 반복합니다.
                handler.postDelayed(this, delay);
            }
        }, delay);
    }



}