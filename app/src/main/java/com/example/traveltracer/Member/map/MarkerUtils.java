package com.example.traveltracer.Member.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class MarkerUtils {
    private locationData locationData;
    private GoogleMap map;

    private Marker CheckpointMarker;

    public MarkerUtils(GoogleMap map) {
        this.map = map;
    }

    public void addMarker(LatLng latLng) {
        map.addMarker(new MarkerOptions().position(latLng));
    }

    public void updateMarker(LatLng latLng) {
        // Marker 업데이트 로직 구현
    }

    public void removeMarker() {
        map.clear();
    }
/*
    public void madeCheckPoint(Location location){
        int count =0;
        //위치 값 비교
        if(locationData.getnewLongitude() == locationData.getlastLongitude()
                && locationData.getnewLatitude() == locationData.getlastLatitude() ){
            count ++;
            if(count == 1200){
                map.addMarker(new MarkerOptions().position(latLng));
            }
        }
        else {
            count = 0;
        }


    }
    private void createCheckpoint() {
        // 체크포인트 생성 코드 작성
        // 위치 정보와 시간 정보를 저장하여 체크포인트 생성
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Checkpoint")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        CheckpointMarker = Map.addMarker(markerOptions);

        return Map.addMarker(markerOptions);
    }
*/
}
