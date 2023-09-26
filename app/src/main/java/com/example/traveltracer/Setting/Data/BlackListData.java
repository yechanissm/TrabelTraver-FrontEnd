package com.example.traveltracer.Setting.Data;

import com.google.gson.annotations.SerializedName;

public class BlackListData {

    @SerializedName("locationId")
    private int locationId;

    @SerializedName("title")
    private String title;

    @SerializedName("address")
    private String address;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;

    public BlackListData() {

    }

    public BlackListData(int locationId, String title, String address, double longitude, double latitude) {
        this.locationId = locationId;
        this.title = title;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
