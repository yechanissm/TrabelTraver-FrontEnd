package com.example.traveltracer.Location.CheckPoint;

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

public class CheckpointManager {

    private TimerManager timermanager;
    private Context context;
    private LocationHelper locationHelper;
    private Location previousLocation;
    private Location currentLocation;
    private GoogleMap map;
    private static final long MINIMUM_UPDATE_INTERVAL = 1 * 60 * 1000; // 20 minutes
    private static final float MINIMUM_DISTANCE_THRESHOLD = 50; // 50 meters
    private HashMap<Location, Long> markerCreationTimes = new HashMap<>(); // 마커가 생성된 시간을 저장하는 맵
    private LocationService service;

    private long currentTime;
    private int locationId =0;

    //CheckpointManger(객체 생성자)
    public CheckpointManager(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
        this.locationHelper = new LocationHelper(context);
        this.service = RetrofitConfig.getLocationService();
        this.timermanager = new TimerManager();
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
                Location newLocation = locationResult.getLastLocation();
                if (currentLocation == null) {  //현재 위치 정보가 없으면 받은 값을 현재 위치 저장
                    currentLocation = newLocation;
                    Log.d("LocationData", "Current " + currentLocation.getLatitude() + currentLocation.getLongitude());
                    updateCheckpoint(currentLocation);
                }else{
                    // 기존 위치와 새 위치를 비교하여 필요한 업데이트를 수행
                    if (newLocation.getLatitude() != currentLocation.getLatitude() && newLocation.getLongitude() != currentLocation.getLongitude()) {
                        currentLocation = newLocation; // 새 위치로 업데이트
                        Log.d("LocationChanged", "Current " + currentLocation.getLatitude() + currentLocation.getLongitude());
                        updateCheckpoint(currentLocation);
                    }
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
    Log.d("UpdateCheckpoint", "Entering updateCheckpoint");
    if (previousLocation != null) {
        Log.d("UpdateCheckpoint", "Previous Latitude: " + previousLocation.getLatitude());
        Log.d("UpdateCheckpoint", "Previous Longitude: " + previousLocation.getLongitude());
    }

    Log.d("UpdateCheckpoint", "Current Latitude: " + currentLocation.getLatitude());
    Log.d("UpdateCheckpoint", "Current Longitude: " + currentLocation.getLongitude());

    if (previousLocation == null) { // 최초 앱을 실행했을 때
        Log.d("UpdateCheckpoint", "previousLocation is null");
        previousLocation = currentLocation; // 이전 위치 정보 저장
        Log.d("UpDateC", "Current " + currentLocation.getLatitude()+" , " + currentLocation.getLongitude());
        Log.d("UpDateP", "Previous " + previousLocation.getLatitude()+" , " + previousLocation.getLongitude());
    }else if(previousLocation.getLatitude() != currentLocation.getLatitude() || previousLocation.getLongitude() != currentLocation.getLongitude()){
        Log.d("UpdateCheckpoint", "Location changed");
        timermanager.locationResetTimer(); // 타이머 리셋
        previousLocation = currentLocation; // 변경된 위치 정보 저장
        Log.d("UpDateCR", "Current " + currentLocation.getLatitude()+" , " + currentLocation.getLongitude());
        Log.d("UpDatePR", "Previous " + previousLocation.getLatitude()+" , " + previousLocation.getLongitude());
    } else /*if (previousLocation.getLatitude() == currentLocation.getLatitude() && previousLocation.getLongitude() == currentLocation.getLongitude())*/
    { // 위치가 변경되었을 때만
        Log.d("UpdateCheckpoint", "Location not changed");
    }

    timermanager.startTimer(() -> {
        Log.d("UpdateCheckpoint", "Creating marker");
        createMarker(previousLocation); // 체크 포인트 생성
    });
}

    // 마커 생성 메소드
    private void createMarker(Location location) {
        currentTime = System.currentTimeMillis();
        if (map != null && isMarkerCreatable(location)) {
            Log.d("UpdateCheckpoint", "Creating marker");
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("체크포인트"); // 마커의 타이틀을 설정할 수 있습니다.
            map.addMarker(markerOptions);
            // 마커 생성 후에 체크포인트 저장 등 추가 작업을 수행할 수 있습니다.
            String savepoint = "savePoint" + locationId;
            savePoint(new CheckPointData(locationId, savepoint, location.getLongitude(), location.getLatitude(), currentTime));
            locationId++;

            // 마커 생성 시간을 저장
            markerCreationTimes.put(location, System.currentTimeMillis());
        }else{
            Log.d("UpdateCheckpoint", "Marker not created: isMarkerCreatable(location) is false");
        }
    }

    // 마커 생성 조건 검사
    private boolean isMarkerCreatable(Location location) {
        // 같은 위치에 마커 중복 생성 방지
        for (Location markerLatLng : markerCreationTimes.keySet()) {
            if (calculateDistance(markerLatLng, location) < MINIMUM_DISTANCE_THRESHOLD) {
                return false;
            }
        }

        // 한 위치에 20분 이상 머무르지 않으면 생성되지 않음
        if (markerCreationTimes.containsKey(location)) {
            long creationTime = markerCreationTimes.get(location);
            long currentTime = System.currentTimeMillis();
            if (currentTime - creationTime < MINIMUM_UPDATE_INTERVAL) {
                return false;
            }
        }

        return true;
    }

    // 두 지점 간의 거리를 계산하는 메소드
    private float calculateDistance(Location location1, Location location2) {
        float[] results = new float[1];
        Location.distanceBetween(location1.getLatitude(), location1.getLongitude(), location2.getLatitude(), location2.getLongitude(), results);
        return results[0];
    }


    private void savePoint(CheckPointData CheckPointData){
        if (context == null) {
            Log.e("savePoint", "Context is null, cannot show Toast.");
            return; // context가 null이면 Toast를 생성하지 않고 함수 종료
        }

        service.CheckPointSave(CheckPointData).enqueue(new Callback<CommonResponse>(){

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



}