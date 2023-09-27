package com.example.traveltracer.Setting.Service;

import com.example.traveltracer.Location.Data.CheckPointData;
import com.example.traveltracer.Setting.Data.BlackListData;
import com.example.traveltracer.Member.Response.CommonResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BlackListService {

    @POST("/Create_Black_List")
    Call<CommonResponse> BlackListSave(@Body BlackListData BlackListData);
}
