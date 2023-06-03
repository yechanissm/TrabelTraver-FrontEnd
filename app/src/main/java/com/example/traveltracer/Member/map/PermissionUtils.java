package com.example.traveltracer.Member.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.example.traveltracer.Member.activity.Main;

public class PermissionUtils {

    private Context context;

    public PermissionUtils(Context context) {
        this.context = context;
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

}