package com.example.traveltracer.Member.Data;

import com.google.gson.annotations.SerializedName;

public class FindIdData {

    @SerializedName("userName")
    private String userName;

    @SerializedName("userEmail")
    private String userEmail;

    public FindIdData(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public FindIdData() {
    }
}
