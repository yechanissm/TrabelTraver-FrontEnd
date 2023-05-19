package com.example.traveltracer.Member.Data;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("userId")
    private String userId;

    @SerializedName("userPassword")
    private String userPassword;

    public LoginData() {

    }

    public LoginData(String userId, String userPassword) {
        this.userId = userId;
        this.userPassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
