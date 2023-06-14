package com.example.traveltracer.Member.map;
import android.location.Location;
import android.os.CountDownTimer;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MyLocationCallback extends LocationCallback {

    private Location previousLocation;
    private CountDownTimer timer;
    public LatLng newlatLng;
    public LatLng previouslatLng;

    private GoogleMap map;
    private MarkerUtils markerUtils;

    public MyLocationCallback(GoogleMap map) {
        this.map = map;
        this.markerUtils = new MarkerUtils(map);
    }
    @Override
    public void onLocationResult(LocationResult locationResult) {
        locationData locationdata = new locationData();
        if (locationResult != null) {
            Location newLocation = locationResult.getLastLocation();
            locationdata.setnewLongitude(newLocation.getLongitude());
            locationdata.setnewLatitude(newLocation.getLatitude());

            // 이전 위치가 null이거나 새로운 위치와의 거리가 50m 이상인 경우
            if (previousLocation == null || previousLocation.distanceTo(newLocation) >= 50) {
                previousLocation = newLocation; // 새로운 위치 정보를 저장
                locationdata.setlastLongitude(previousLocation.getLongitude());
                locationdata.setlastLatitude(previousLocation.getLatitude());
                // 타이머를 시작하고 진행합니다.
                startTimer(previouslatLng);
            }
        }
    }


    private void startTimer(LatLng previouslatLng) {
        if (timer != null) {
            timer.cancel(); // 기존의 타이머가 있다면 취소합니다.
        }

        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                // 타이머가 종료된 후의 동작
                if (previousLocation != null) {
                    LatLng checkpointLatLng = new LatLng(previousLocation.getLatitude(), previousLocation.getLongitude());
                    markerUtils.createCheckpoint(checkpointLatLng);
                }
            }
        };

        timer.start(); // 타이머를 시작합니다.
    }
}
