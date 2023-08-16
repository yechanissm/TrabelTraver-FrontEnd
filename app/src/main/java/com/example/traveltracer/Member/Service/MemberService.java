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

    //@get :정보만 전달
    //@post : 정보를 보내서 결과값 받음
    //check /{우리가 받을 id값}
    //(@Path("userId") String userId) 여기서 @Path("userId")에서 값은 String userId 이걸로 사용하겠다.
    @GET("/check/{userId}")
    Call<CommonResponse> checkIdExist(@Path("userId") String userId);


    //@header : 보낼 정보 타입
    //@Body : 내가 보낼 데이타
    @Headers("Content-Type: application/json")
    @POST("/login")
    Call<LoginResponse> login(@Body LoginData loginData);


    @POST("/findId")
    Call<CommonResponse> findId(@Body FindIdData findIdData);

    @POST("/findPw")
    Call<CommonResponse> findPw(@Body FindPwData findPwData);
}
