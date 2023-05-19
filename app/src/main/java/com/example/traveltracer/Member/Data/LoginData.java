package com.example.traveltracer.Member.Data;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("userId")
    private String userId;

    @SerializedName("userPassword")
    private String userPassword;

    @SerializedName("message")
    private String message;

    public LoginData() {

    }

    public String getMessage() {
        return message;
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
