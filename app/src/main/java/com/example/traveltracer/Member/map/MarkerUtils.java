package com.example.traveltracer.Member.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerUtils {
    private GoogleMap map;

    private Marker CheckpointMarker;

    public MarkerUtils(GoogleMap map) {
        this.map = map;
    }

   /* public void addMarker(LatLng latLng) {
        map.addMarker(new MarkerOptions().position(latLng));
    }*/


    Marker createCheckpoint(LatLng latLng) {

        // 위치 정보와 시간 정보를 저장하여 체크포인트 생성
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Checkpoint")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        return map.addMarker(markerOptions);
    }

}
