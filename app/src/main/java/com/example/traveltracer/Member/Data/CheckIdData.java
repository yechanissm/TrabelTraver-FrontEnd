package com.example.traveltracer.Member.Data;

import com.google.gson.annotations.SerializedName;

public class CheckIdData {

    @SerializedName("userId")
    private String userId;

    public CheckIdData(String userId) {
        this.userId = userId;
    }
}
