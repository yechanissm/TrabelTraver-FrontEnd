package com.example.traveltracer.Member.map;
import android.location.Location;
import android.os.Handler;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CheckpointManager {
    private static final long UPDATE_INTERVAL = 1000; // 위치 업데이트 간격 (1초)
    private static final float CHECKPOINT_RADIUS = 50; // 체크포인트 반경 (50m)
    private static final long CHECKPOINT_DURATION = 20 * 60 * 1000; // 체크포인트 생성 기준 시간 (20분)

    private Location previousLocation; // 이전 위치 정보
    private boolean isInsideCheckpointArea; // 특정 위치 반경 내에 있는지 여부
    private long checkpointTimer; // 체크포인트 타이머
    private Handler handler;



    public CheckpointManager() {
        handler = new Handler();
    }

    // 위치 업데이트를 받았을 때 호출되는 메소드
    public void onLocationUpdate(Location currentLocation) {
        if (previousLocation == null) {
            previousLocation = currentLocation;
            return;
        }

        // 이전 위치와 현재 위치의 거리 계산
        float distance = previousLocation.distanceTo(currentLocation);

        if (distance <= CHECKPOINT_RADIUS) {
            // 특정 위치 반경 내에 있음
            if (!isInsideCheckpointArea) {
                // 타이머 시작
                startTimer();
                isInsideCheckpointArea = true;
            }
        } else {
            // 특정 위치 반경 밖에 있음
            if (isInsideCheckpointArea) {
                // 타이머 중지
                stopTimer();
                isInsideCheckpointArea = false;
            }
        }

        previousLocation = currentLocation;
    }

    // 타이머 시작
    private void startTimer() {
        handler.postDelayed(checkpointRunnable, CHECKPOINT_DURATION);
        checkpointTimer = System.currentTimeMillis();
    }

    // 타이머 중지
    private void stopTimer() {
        handler.removeCallbacks(checkpointRunnable);
        checkpointTimer = 0;
    }

        // 체크포인트 생성
        private void createCheckpoint() {
            // 체크포인트 생성 코드 작성
            // 위치 정보와 시간 정보저장하여 체크포인트 생성
    /*
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title("Checkpoint")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            CheckpointMarker = Map.addMarker(markerOptions);

            return Map.addMarker(markerOptions);*/
        }

    // 타이머 실행 중 체크포인트 생성 여부를 확인하는 Runnable
    private Runnable checkpointRunnable = new Runnable() {
        @Override
        public void run() {
            long elapsedTime = System.currentTimeMillis() - checkpointTimer;
            if (elapsedTime >= CHECKPOINT_DURATION) {
                createCheckpoint();
            } else {
                // 타이머 재시작
                startTimer();
            }
        }
    };
}
