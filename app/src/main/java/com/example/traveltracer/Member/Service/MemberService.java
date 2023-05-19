package com.example.traveltracer.Member.Service;

import com.example.traveltracer.Member.Data.LoginData;
import com.example.traveltracer.Member.Data.SignUpdata;
import com.example.traveltracer.Member.Response.CommonResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MemberService {

    @POST("/signUp")
    Call<CommonResponse> userSignUp(@Body SignUpdata signUpdata);

    @GET("/check/{userId}")
    Call<CommonResponse> checkIdExist(@Path("userId") String userId);

    @POST("/login")
    Call<CommonResponse> login(@Body LoginData loginData);
}
