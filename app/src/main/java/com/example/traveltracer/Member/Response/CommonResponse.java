package com.example.traveltracer.Member.Response;

import com.google.gson.annotations.SerializedName;

public class CommonResponse<T> {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;


    //code : 성공 여부 코드
    //message : 성공 여부에 대한 출력값
    public CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
