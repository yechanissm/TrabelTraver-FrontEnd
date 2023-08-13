package com.example.traveltracer.Location.Data;


import com.google.gson.annotations.SerializedName;

public class locationData {

    @SerializedName("locationId")
    private int locationId;

    @SerializedName("locatoinName")
    private String locationName;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("createYear")
    private int createYear;

    @SerializedName("createMonth")
    private int createMonth;


    public void CheckPointData(){

    }
    public void CheckPointData(int locationId, String locationName, double longitude, double latitude){
        this.locationId = locationId;
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
    }


}