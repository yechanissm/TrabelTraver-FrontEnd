package com.example.traveltracer.Member.map;

import android.location.Location;

public class locationData {

    private double newlongitude;
    private double newlatitude;

    private double lastlongitude;
    private double lastlatitude;

    public void setnewLatitude(double latitude) {

        this.newlatitude = latitude;
    }

    public void setnewLongitude(double longitude) {

        this.newlongitude = longitude;
    }

    public double getnewLatitude() {

        return newlatitude;
    }

    public double getnewLongitude() {
        return newlongitude;
    }
    public void setlastLatitude(double latitude) {

        this.lastlatitude = latitude;
    }

    public void setlastLongitude(double longitude) {

        this.lastlongitude = longitude;
    }

    public double getlastLatitude() {

        return lastlatitude;
    }

    public double getlastLongitude() {

        return lastlongitude;
    }

}