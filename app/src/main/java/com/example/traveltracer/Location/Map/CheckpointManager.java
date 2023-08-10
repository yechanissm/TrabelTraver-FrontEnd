package com.example.traveltracer.Location.Map;

import android.content.Context;
import android.location.Location;
import android.os.Handler;

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

public class CheckpointManager {

    private Context context;
    private LocationHelper locationHelper;
    private Location previousLocation;
    private List<LatLng> checkpoints;
    private Marker checkpointMarker;
    private GoogleMap map;

    private boolean isFirstLocationUpdate = true;
    private LocationCallback locationCallback;
    private static final long MINIMUM_UPDATE_INTERVAL = 3 * 60 * 1000; // 20 minutes
    private static final float MINIMUM_DISTANCE_THRESHOLD = 50; // 50 meters
    private static final long STAY_DURATION = 3 * 60 * 1000; // 20 minutes
    private HashMap<Location, Long> markerCreationTimes = new HashMap<>(); // 마커가 생성된 시간을 저장하는 맵

    private Handler handler;
    private Runnable timerRunnable;

    //CheckpointManger(객체 생성자)
    public CheckpointManager(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
        this.locationHelper = new LocationHelper(context);
        this.checkpoints = new ArrayList<>();
        this.handler = new Handler();

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
        long currentTime = System.currentTimeMillis();
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
                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(previousLocation.getLatitude(), previousLocation.getLongitude()));
                    map.addMarker(markerOptions);
                }
            }
        }

        // 예약된 마커 생성 실행
        for (Location locationToRemove : locationsToRemove) {
            markerCreationTimes.remove(locationToRemove);
        }
    }


}