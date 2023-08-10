package com.example.traveltracer.Member.Service;

import com.example.traveltracer.Member.Data.FindIdData;
import com.example.traveltracer.Member.Data.FindPwData;
import com.example.traveltracer.Member.Data.LoginData;
import com.example.traveltracer.Member.Data.SignUpdata;
import com.example.traveltracer.Member.Response.CommonResponse;
import com.example.traveltracer.Member.Response.LoginResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MemberService {

    //post를 통해 retrofit 주소 연결 뒷부분에 해당 (즉 동작 하는 부분으로 연결해주는 부분ㄴ)
    @POST("/signUp")
    Call<CommonResponse> userSignUp(@Body SignUpdata signUpdata);

    @GET("/check/{userId}")
    Call<CommonResponse> checkIdExist(@Path("userId") String userId);


    @Headers("Content-Type: application/json")
    @POST("/login")
    Call<LoginResponse> login(@Body LoginData loginData);

    @POST("/findId")
    Call<CommonResponse> findId(@Body FindIdData findIdData);

    @POST("/findPw")
    Call<CommonResponse> findPw(@Body FindPwData findPwData);
}
