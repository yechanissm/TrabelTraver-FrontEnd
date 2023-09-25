package com.example.traveltracer.Location.BlackList.Data;

import com.google.gson.annotations.SerializedName;

public class blacklistData {

    @SerializedName("blackListId")
    private int blackListId;

    @SerializedName("locatoinName")
    private String locationName;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("userId")
    private String userId;

}
