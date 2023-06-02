package com.example.traveltracer.Member.Data;

import com.google.gson.annotations.SerializedName;

public class FindPwData {

    @SerializedName("userId")
    private String userId;

    @SerializedName("userName")
    private String userName;

    @SerializedName("userEmail")
    private String userEmail;

    public FindPwData() {
    }

    public FindPwData(String userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
