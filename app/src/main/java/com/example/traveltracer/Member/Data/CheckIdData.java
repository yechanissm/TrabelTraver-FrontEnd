package com.example.traveltracer.Member.Data;

import com.google.gson.annotations.SerializedName;

public class CheckIdData {

    //Gson 라이브러리에서 사용되는 어노테이션으로, JSON 데이터의 특정 필드와 클래스의 멤버 변수를 매핑시키는 역할
    @SerializedName("userId")
    private String userId;

    public CheckIdData(String userId) {
        this.userId = userId;
    }
}
