package com.example.traveltracer.Location.service;

import com.example.traveltracer.Location.Data.CheckPointData;
import com.example.traveltracer.Member.Response.CommonResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LocationService {

    @POST("/CheckpointManager")
    Call<CommonResponse> CheckPointSave(@Body CheckPointData CheckPointData);


}
