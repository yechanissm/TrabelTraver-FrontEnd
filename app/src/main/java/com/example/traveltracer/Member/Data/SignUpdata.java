package com.example.traveltracer.Member.Data;

import com.google.gson.annotations.SerializedName;

public class SignUpdata {
    @SerializedName("userId")
    private String userId;

    @SerializedName("userPassword")
    private String userPassword;

    @SerializedName("userName")
    private String userName;

    @SerializedName("age")
    private Integer age;

    public SignUpdata() {
    }

    public SignUpdata(String userId, String userPassword, String userName, Integer age) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.age = age;
    }

}
