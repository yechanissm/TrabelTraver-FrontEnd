package com.example.traveltracer.Member.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerUtils {
    private locationData locationData;
    private GoogleMap map;

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
 */
}
