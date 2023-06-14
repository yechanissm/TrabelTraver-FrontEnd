package com.example.traveltracer.Member.map;

import android.content.Context;
import android.location.Location;
import android.os.Handler;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class CheckpointManager {

    private Context context;
    private LocationHelper locationHelper;
    private Location previousLocation;
    private List<LatLng> checkpoints;
    private Marker checkpointMarker;
    private GoogleMap map;

    private boolean isFirstLocationUpdate = true;

    private static final long MINIMUM_UPDATE_INTERVAL = 3 * 60 * 1000; // 20 minutes
    private static final float MINIMUM_DISTANCE_THRESHOLD = 50; // 50 meters

    private Handler handler;
    private Runnable timerRunnable;

    public CheckpointManager(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
        this.locationHelper = new LocationHelper(context);
        this.checkpoints = new ArrayList<>();
        this.handler = new Handler();
    }

    public void setupCheckpoint() {
        locationHelper.getLastKnownLocation(new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location lastKnownLocation = locationResult.getLastLocation();
                    if (lastKnownLocation != null) {
                        previousLocation = lastKnownLocation;
                    }
                }
                startTimer();
            }
        });
    }

    private void updateCheckpoint(Location currentLocation) {
        if (previousLocation == null) {
            // 최초 위치 정보
            saveNewCheckpoint(currentLocation);
        } else {
            float distance = currentLocation.distanceTo(previousLocation);
            long timeDifference = currentLocation.getTime() - previousLocation.getTime();

            if (distance >= MINIMUM_DISTANCE_THRESHOLD || timeDifference >= MINIMUM_UPDATE_INTERVAL) {
                // 이전 위치 정보와의 거리가 50m 이상이거나
                // 첫 위치 업데이트이면서 시간 차이가 20분 이상인 경우에만 마커 생성
                saveNewCheckpoint(currentLocation);
            }
        }

        previousLocation = currentLocation;
    }

    private void saveNewCheckpoint(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        checkpoints.add(latLng);

        // 마커 생성 코드
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        map.addMarker(markerOptions);
    }

    private void startTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                // 이전 위치와 현재 위치를 비교하여 마커 생성 여부 결정
                locationHelper.getCurrentLocation(new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult != null) {
                            Location currentLocation = locationResult.getLastLocation();
                            if (currentLocation != null) {
                                float distance = currentLocation.distanceTo(previousLocation);
                                long timeDifference = currentLocation.getTime() - previousLocation.getTime();

                                if (distance >= MINIMUM_DISTANCE_THRESHOLD || timeDifference >= MINIMUM_UPDATE_INTERVAL) {
                                    saveNewCheckpoint(currentLocation);
                                    previousLocation = currentLocation;
                                }
                            }
                        }
                    }
                });

                // 다음 타이머 실행을 위해 타이머 재등록
                handler.postDelayed(this, 3 * 60 * 1000);
            }
        };

        // 최초 실행을 위해 타이머 시작
        handler.post(timerRunnable);
    }
}
