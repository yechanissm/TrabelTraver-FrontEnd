package com.example.traveltracer.Member.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.traveltracer.Member.activity.Main;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationUtils {

    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private locationData locationData;

    public LocationUtils(Context context) {
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Main) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Main.LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    public void requestLocationUpdates(LocationRequest locationRequest, LocationCallback locationCallback) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        } else {
            ActivityCompat.requestPermissions((Main) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Main.LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //최신화 안될때 마지막 정보로 위치 보냄
    public void stopLocationUpdates(LocationCallback locationCallback) {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    public void displayCurrentLocation(Location location) {
        //location 객체 새로 생성하여 초기화
        locationData = new locationData();
        // 최근에 받은 초기화전 저장된 new위치 정보 last 값으로 저장
        locationData.setlastLatitude(locationData.getnewLatitude());
        locationData.setlastLongitude(locationData.getnewLongitude());
        GoogleMap map = ((Main) context).map;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.clear();
        map.addMarker(new MarkerOptions().position(latLng));
        //새로운 위치 정보 new에 저장
        locationData.setnewLatitude(location.getLatitude());
        locationData.setnewLongitude(locationData.getnewLongitude());
    }
    public void CurrentLocationMove(Location location) {
        GoogleMap map = ((Main) context).map;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.clear();
        map.addMarker(new MarkerOptions().position(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}
