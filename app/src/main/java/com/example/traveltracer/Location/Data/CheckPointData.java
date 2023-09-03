package com.example.traveltracer.Location.Data;


import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class CheckPointData {

    @SerializedName("locationId")
    private int locationId;

    @SerializedName("locatoinName")
    private String locationName;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("createTime")
    private long createtime;


    public CheckPointData(){

    }
    public CheckPointData(int locationId, String locationName, double longitude, double latitude, long createtime){
        this.locationId = locationId;
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.createtime = createtime;
    }


}